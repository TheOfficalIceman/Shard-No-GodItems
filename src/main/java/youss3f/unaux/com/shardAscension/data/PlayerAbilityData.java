package youss3f.unaux.com.shardascension.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PlayerAbilityData {

    private final UUID uuid;
    private final List<String> abilityIds;
    private String selectedAbilityId;
    private int shards;

    public PlayerAbilityData(UUID uuid, List<String> abilityIds, String selectedAbilityId, int shards) {
        this.uuid = uuid;
        this.abilityIds = new ArrayList<>(abilityIds);
        this.selectedAbilityId = selectedAbilityId;
        this.shards = shards;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<String> getAbilityIds() {
        return abilityIds;
    }

    public String getSelectedAbilityId() {
        return selectedAbilityId;
    }

    public void setSelectedAbilityId(String selectedAbilityId) {
        this.selectedAbilityId = selectedAbilityId;
    }

    public int getShards() {
        return shards;
    }

    public void setShards(int shards) {
        this.shards = Math.min(3, Math.max(0, shards));
    }
}
