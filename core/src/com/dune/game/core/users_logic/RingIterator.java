package com.dune.game.core.users_logic;

import java.util.function.Consumer;

/* Инкапсулирует метод для обхода элементов двумерного массива, расположенных "кольцом" вокруг заданного элемента */
class RingIterator <T> {
    private final T[][] arr;
    private final int MAX_X;
    private final int MAX_Y;

    public RingIterator(T[][] arr) {
        this.arr = arr;
        MAX_X = arr.length - 1;
        MAX_Y = arr[0].length - 1;
    }

    /**
     * Обойти элементы, расположенные на "кольце" радиуса r вокруг базового элемента.
     * iterateAroundElement(2, 2, 1, f): https://prnt.sc/szl78d
     * @param x0 Координата x базового элемента
     * @param y0 Координата y базового элемента
     * @param r радиус "кольца"
     * @param f функция, применяемая к каждому элементу "кольца" при обходе
     */
    public void iterateAroundElement(int x0, int y0, int r, Consumer<T> f) {
        /* todo: избавиться от перебора индексов за границами arr. */
        int x = x0; int y = y0;
        if (r == 0) {
            f.accept(arr[x][y]);
            return;
        }

        x += r; y -= r; // Начало обхода в правом нижнем углу

        for (int i = 0; i < 2 * r + 1; i++) {
            if (x >= 0 && x <= MAX_X && y >= 0 && y <= MAX_Y) {
                f.accept(arr[x][y]);
            }
            ++y;
        }
        --y;

        for (int i = 0; i < 2 * r; i++) {
            --x;
            System.out.println(x);
            if (x >= 0 && x <= MAX_X && y >= 0 && y <= MAX_Y) {
                f.accept(arr[x][y]);
            }
        }

        for (int i = 0; i < 2 * r; i++) {
            --y;
            if (x >= 0 && x <= MAX_X && y >= 0 && y <= MAX_Y) {
                f.accept(arr[x][y]);
            }
        }

        for (int i = 0; i < 2 * r - 1; i++) {
            ++x;
            if (x >= 0 && x <= MAX_X && y >= 0 && y <= MAX_Y) {
                f.accept(arr[x][y]);
            }
        }
    }

}