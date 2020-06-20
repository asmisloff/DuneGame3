package com.dune.game.core.users_logic;

import com.dune.game.core.BattleMap;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
* Решает задачу поиска незанятой ресурсной клетки в окрестности заданной клетки карты.
 * Найденная клетка не является ближайшей, она просто будет неподалеку.
*/
public class ResourceFinder extends RingIterator<BattleMap.Cell> {
    final private ArrayList<BattleMap.Cell> foundCells;
    final private ArrayList<BattleMap.Cell> reservedCells;
    final private Consumer<BattleMap.Cell> f;

    public ResourceFinder(BattleMap.Cell[][] cells) {
        super(cells);
        foundCells = new ArrayList<>();
        reservedCells = new ArrayList<>();

        f = new Consumer<BattleMap.Cell>() {
            @Override
            public void accept(BattleMap.Cell cell) {
                if (cell.getResource() > 0) {
                    foundCells.add(cell);
                }
            }
        };
    }

    public void iterateAroundElement(int x0, int y0, int r) {
        foundCells.clear();
        super.iterateAroundElement(x0, y0, r, f);
    }

    public BattleMap.Cell selectCellFromFoundedAndReserve() {
        for (int i = 0; i < foundCells.size(); i++) {
            BattleMap.Cell c = foundCells.get(i);
            if (!reservedCells.contains(c)) {
                reservedCells.add(c);
                return c;
            }
        }
        return null;
    }

    public void init() {
        reservedCells.clear();
    }
}
