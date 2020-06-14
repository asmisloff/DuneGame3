package com.dune.game.core.users_logic;

import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.BattleMap;
import com.dune.game.core.Building;
import com.dune.game.core.GameController;
import com.dune.game.core.units.AbstractUnit;
import com.dune.game.core.units.BattleTank;
import com.dune.game.core.units.Harvester;
import com.dune.game.core.units.types.Owner;
import com.dune.game.core.units.types.UnitType;

import java.util.ArrayList;
import java.util.List;

public class AiLogic extends BaseLogic {
    private float timer;

    private List<BattleTank> tmpAiBattleTanks;
    private List<Harvester> tmpAiHarvesters;
    private List<Harvester> tmpPlayerHarvesters;
    private List<Harvester> tmpPlayerBattleTanks;
    final private Vector2 tmp;

    private ResourceFinder rf;

    public AiLogic(GameController gc) {
        this.gc = gc;
        this.money = 1000;
        this.unitsCount = 10;
        this.unitsMaxCount = 100;
        this.ownerType = Owner.AI;
        this.tmpAiBattleTanks = new ArrayList<>();
        this.tmpPlayerHarvesters = new ArrayList<>();
        this.tmpPlayerBattleTanks = new ArrayList<>();
        this.tmpAiHarvesters = new ArrayList<>();
        this.timer = 10000.0f;
        tmp = new Vector2();
    }

    public void update(float dt) {
        timer += dt;
        if (timer > 2.0f) {
            timer = 0.0f;
            gc.getUnitsController().collectTanks(tmpAiBattleTanks, gc.getUnitsController().getAiUnits(), UnitType.BATTLE_TANK);
            gc.getUnitsController().collectTanks(tmpPlayerHarvesters, gc.getUnitsController().getPlayerUnits(), UnitType.HARVESTER);
            gc.getUnitsController().collectTanks(tmpPlayerBattleTanks, gc.getUnitsController().getPlayerUnits(), UnitType.BATTLE_TANK);
            gc.getUnitsController().collectTanks(tmpAiHarvesters, gc.getUnitsController().getAiUnits(), UnitType.HARVESTER);

            updateBattleTanksDisposition();
            updateHarvestersDisposition();
        }
    }

    public <T extends AbstractUnit> T findNearestTarget(AbstractUnit currentTank, List<T> possibleTargetList) {
        T target = null;
        float minDist = 1000000.0f;
        for (int i = 0; i < possibleTargetList.size(); i++) {
            T possibleTarget = possibleTargetList.get(i);
            float currentDst = currentTank.getPosition().dst(possibleTarget.getPosition());
            if (currentDst < minDist) {
                target = possibleTarget;
                minDist = currentDst;
            }
        }
        return target;
    }

    private void updateBattleTanksDisposition() {
        for (int i = 0; i < tmpAiBattleTanks.size(); i++) {
            BattleTank aiBattleTank = tmpAiBattleTanks.get(i);
            aiBattleTank.commandAttack(findNearestTarget(aiBattleTank, tmpPlayerBattleTanks));
        }
    }

    private void updateHarvestersDisposition() {
        if (rf == null) {
            rf = new ResourceFinder(gc.getMap().getCells());
        }
        BattleMap.Cell c;
        rf.init();
        for (int i = 0; i < tmpAiHarvesters.size(); i++) {
            Harvester h = tmpAiHarvesters.get(i);
            if (h.isFull()) {
                Building storage = gc.getBuildingsController().getStorage(this);
                if (storage != null) {
                    h.commandMoveTo(storage.getEntrancePosition());
                }
            } else { /* Найти в окрестности харвестера h ресурсную клетку, не зарезервированную другим харвестером, и отправить h туда пастись */
                for (int j = 0; j < Math.max(BattleMap.ROWS_COUNT, BattleMap.COLUMNS_COUNT); j++) {
                    rf.iterateAroundElement(h.getCellX(), h.getCellY(), j);
                    c = rf.selectCellFromFoundedAndReserve();
                    if (c != null) {
                        h.commandMoveTo(tmp.set(c.getX(), c.getY()));
                        break;
                    }
                }
            }
        }
    }

}
