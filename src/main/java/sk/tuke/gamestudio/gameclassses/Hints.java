package sk.tuke.gamestudio.gameclassses;

import java.io.*;

public class Hints {
    private int numRows;
    private int numCols;
    private String[] rowHints;
    private String[] colHints;

    public Hints(int numRows, int numCols, String[] rowHints, String[] colHints) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.rowHints = rowHints;
        this.colHints = colHints;
    }

    public Hints(String[] rowHints, String[] colHints)
    {
        this.rowHints = rowHints;
        this.colHints = colHints;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public String[] getRowHints() {
        return rowHints;
    }

    public String[] getColHints() {
        return colHints;
    }


    public static Hints readFromFile(String filename) {
        int numRows = 0;
        int numCols = 0;
        String[] rowHints = null;
        String[] colHints = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            if (line != null) {
                String[] dims = line.split(",");
                numRows = Integer.parseInt(dims[0].trim());
                numCols = Integer.parseInt(dims[1].trim());
            }
            rowHints = new String[numRows];
            for (int i = 0; i < numRows; i++) {
                line = br.readLine();

                String[] tokens = line.split(",");
                StringBuilder sb = new StringBuilder();
                for (String token : tokens) {
                    token = token.replace("#", "").trim();
                    if (!token.isEmpty()) {
                        if (sb.length() > 0) {
                            sb.append(" ");
                        }
                        sb.append(token);
                    }
                }
                rowHints[i] = sb.toString();
            }
            colHints = new String[numCols];
            for (int j = 0; j < numCols; j++) {
                line = br.readLine();
                String[] tokens = line.split(",");
                StringBuilder sb = new StringBuilder();
                for (String token : tokens) {
                    token = token.replace("#", "").trim();
                    if (!token.isEmpty()) {
                        if (sb.length() > 0) {
                            sb.append(" ");
                        }
                        sb.append(token);
                    }
                }
                colHints[j] = sb.toString();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new Hints(numRows, numCols, rowHints, colHints);
    }
    public static Hints readFromInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String header = reader.readLine();
            if (header == null) throw new IOException("Header missing in puzzle");

            String[] sizes = header.split(",");
            int rowCount = Integer.parseInt(sizes[0].trim());
            int colCount = Integer.parseInt(sizes[1].trim());

            String[] rowHints = new String[rowCount];
            for (int i = 0; i < rowCount; i++) {
                rowHints[i] = reader.readLine().trim();
            }

            String[] colHints = new String[colCount];
            for (int i = 0; i < colCount; i++) {
                colHints[i] = reader.readLine().trim();
            }

            return new Hints(rowHints, colHints);
        }
    }



}