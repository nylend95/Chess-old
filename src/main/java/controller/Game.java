package main.java.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.java.model.Board;
import main.java.model.Player;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jesper Nylend on 10.02.2017.
 * s305070
 */
public class Game implements Initializable {
    private static final double CELL_SIZE = 98;
    private Board board; //Column | Row
    private Player p1;
    private Player p2;


    @FXML
    Canvas cv;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        board = new Board();

        GraphicsContext gc = cv.getGraphicsContext2D();
        initDraw(gc);
    }

    private void initDraw(GraphicsContext gc) {
        for (int c = 0; c < 9; c++) {
            for (int r = 0; r < 9; r++) {
                gc.strokeLine(r * 100, 0, r * 100, 800);
            }
        }
        for (int c = 0; c < 9; c++) {
            for (int r = 0; r < 9; r++) {
                gc.strokeLine(0, r * 100, 800, r * 100);
            }
        }
        for (int c = 0; c < 900; c += 100) {
            if ((c - 100) % 200 != 0) {
                for (int r = 0; r < 900; r += 100) {
                    if (r % 200 != 0) {
                        gc.setFill(Color.GREY);
                        gc.fillRect(c + 1, r + 1, CELL_SIZE, CELL_SIZE);
                    }
                }
            } else {
                for (int r = 0; r < 900; r += 100) {
                    if (r % 200 == 0) {
                        gc.setFill(Color.GREY);
                        gc.fillRect(c + 1, r + 1, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    private void draw(GraphicsContext gc) {


//        for (int c = 0; c < board.getBoard().length; c++) {
//            for (int r = 0; r < board.getBoard()[0].length; r++) {
//
//            }
//        }

    }
}
