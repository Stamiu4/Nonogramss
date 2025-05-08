package sk.tuke.gamestudio.gameclassses;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;



public class SolverN {

    private int num_rows, num_cols;
    private Variable[] rows, cols;
    private Queue<Variable> variables;
    private List<String> solutions;
    private Hints hints;
    private int sizeX;
    private int sizeY;

    public SolverN() {
        this.variables = new PriorityQueue<>(new variable_comparator());
        this.solutions = new ArrayList<>();
    }


    public void solveTaskFromFile(String filename) {
        try {
            loadTaskFromFile(filename);
        } catch (IOException exception) {
            System.out.println("IOException: " + exception.toString());
            System.exit(-1);
        }
        this.backtracking_search();
    }


    private void loadTaskFromFile(String filename) throws IOException {
        int line_number = 0;
        BufferedReader buf_read = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = buf_read.readLine()) != null) {
            String[] tokens = line.split(",");
            if (line_number == 0) {

                this.num_rows = Integer.parseInt(tokens[0]);
                this.num_cols = Integer.parseInt(tokens[1]);
                this.rows = new Variable[this.num_rows];
                this.cols = new Variable[this.num_cols];
            } else if (line_number > 0 && line_number <= this.num_rows) {

                int id = line_number - 1;
                this.rows[id] = new Variable(id, this.num_cols, true, false);
                for (int i = 0; i < tokens.length; i += 2) {
                    char color = tokens[i].charAt(0);
                    int length = Integer.parseInt(tokens[i + 1]);
                    this.rows[id].set_constraint(new Constraint(length, color));
                }
                this.rows[id].set_domain();
            } else if (line_number > this.num_rows && line_number <= this.num_rows + this.num_cols) {

                int id = line_number - this.num_rows - 1;
                this.cols[id] = new Variable(id, this.num_rows, false, true);
                for (int i = 0; i < tokens.length; i += 2) {
                    char color = tokens[i].charAt(0);
                    int length = Integer.parseInt(tokens[i + 1]);
                    this.cols[id].set_constraint(new Constraint(length, color));
                }
                this.cols[id].set_domain();
            }
            line_number++;
        }
        buf_read.close();
    }

    private void backtracking_search() {
        this.recursive_backtracking(0);
    }

    private void recursive_backtracking(int step) {
        this.sort_variables();
        if (this.variables.isEmpty()) {
            this.add_solution();
            return;
        }
        Variable var = this.variables.poll();
        for (Value val : var.get_domain()) {
            this.assign_value(var, val);
            if (this.forward_checking(var, step)) {
                this.arc_consistency(step);
                this.recursive_backtracking(step + 1);
            }
            this.step_back(step);
            this.assign_value(var, null);
        }
    }

    private void sort_variables() {
        this.variables.clear();
        for (Variable row : this.rows) {
            if (row.get_value() == null) {
                this.variables.add(row);
            }
        }
        for (Variable col : this.cols) {
            if (col.get_value() == null) {
                this.variables.add(col);
            }
        }
    }

    private void assign_value(Variable var, Value val) {
        if (var.is_row()) {
            this.rows[var.get_id()].set_value(val);
        }
        if (var.is_col()) {
            this.cols[var.get_id()].set_value(val);
        }
    }

    private boolean forward_checking(Variable var, int step) {
        if (var.is_row()) {
            Value val_1 = this.rows[var.get_id()].get_value();
            for (Variable col : this.cols) {
                if (col.get_value() == null) {
                    Iterator<Value> domain = col.get_domain().iterator();
                    while (domain.hasNext()) {
                        Value val_2 = domain.next();
                        if (val_1.get()[col.get_id()] != val_2.get()[var.get_id()]) {
                            col.push_to_removed(val_2, step);
                            domain.remove();
                        }
                    }
                    if (col.get_domain().isEmpty()) {
                        return false;
                    }
                }
            }
        }
        if (var.is_col()) {
            Value val_1 = this.cols[var.get_id()].get_value();
            for (Variable row : this.rows) {
                if (row.get_value() == null) {
                    Iterator<Value> domain = row.get_domain().iterator();
                    while (domain.hasNext()) {
                        Value val_2 = domain.next();
                        if (val_1.get()[row.get_id()] != val_2.get()[var.get_id()]) {
                            row.push_to_removed(val_2, step);
                            domain.remove();
                        }
                    }
                    if (row.get_domain().isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void arc_consistency(int step) {
        Stack<Arc> stack = new Stack<>();
        for (Variable row : this.rows) {
            for (Variable col : this.cols) {
                if (row.get_value() == null && col.get_value() == null) {
                    stack.push(new Arc(row, col));
                    stack.push(new Arc(col, row));
                }
            }
        }
        while (!stack.isEmpty()) {
            Arc arc = stack.pop();
            if (arc.remove_inconsistent(step)) {
                if (arc.get_var1().is_row()) {
                    for (Variable col : this.cols) {
                        if (col.get_value() == null) {
                            stack.push(new Arc(col, arc.get_var1()));
                        }
                    }
                }
                if (arc.get_var1().is_col()) {
                    for (Variable row : this.rows) {
                        if (row.get_value() == null) {
                            stack.push(new Arc(row, arc.get_var1()));
                        }
                    }
                }
            }
        }
    }

    private void step_back(int step) {
        for (Variable col : this.cols) {
            col.pop_from_removed(step);
        }
        for (Variable row : this.rows) {
            row.pop_from_removed(step);
        }
    }

    private void add_solution() {
        String solution = "";
        for (Variable row : this.rows) {
            solution += String.valueOf(row.get_value().get()) + "\n";
        }
        this.solutions.add(solution);
    }


    public String getFirstSolution() {
        if (this.solutions.isEmpty()) {
            return null;
        }
        return this.solutions.get(0);
    }
    public void solve(List<String> rowHints, List<String> colHints, int numRows, int numCols) {
        this.num_rows = numRows;
        this.num_cols = numCols;
        this.rows = new Variable[num_rows];
        this.cols = new Variable[num_cols];

        // Инициализация строк
        for (int i = 0; i < num_rows; i++) {
            this.rows[i] = new Variable(i, num_cols, true, false);
            String[] tokens = rowHints.get(i).split(",");
            for (int j = 0; j < tokens.length; j += 2) {
                char color = tokens[j].trim().charAt(0);
                int length = Integer.parseInt(tokens[j + 1].trim());
                this.rows[i].set_constraint(new Constraint(length, color));
            }
            this.rows[i].set_domain();
        }

        // Инициализация столбцов
        for (int i = 0; i < num_cols; i++) {
            this.cols[i] = new Variable(i, num_rows, false, true);
            String[] tokens = colHints.get(i).split(",");
            for (int j = 0; j < tokens.length; j += 2) {
                char color = tokens[j].trim().charAt(0);
                int length = Integer.parseInt(tokens[j + 1].trim());
                this.cols[i].set_constraint(new Constraint(length, color));
            }
            this.cols[i].set_domain();
        }

        this.backtracking_search();
    }


    private class variable_comparator implements Comparator<Variable> {
        @Override
        public int compare(Variable var_1, Variable var_2) {
            return Integer.compare(var_1.get_domain().size(), var_2.get_domain().size());
        }
    }
    public void solveTaskFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        List<String> lines = reader.lines().collect(Collectors.toList());

        if (lines.isEmpty()) throw new IOException("Пустой файл");

        String header = lines.get(0).trim();
        String[] parts = header.split(",");
        int numRows = Integer.parseInt(parts[0].trim());
        int numCols = Integer.parseInt(parts[1].trim());

        List<String> rowHints = lines.subList(1, 1 + numRows);
        List<String> colHints = lines.subList(1 + numRows, 1 + numRows + numCols);

        solve(rowHints, colHints, numRows, numCols);
    }



}
