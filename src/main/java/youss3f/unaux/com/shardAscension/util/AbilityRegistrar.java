package youss3f.unaux.com.shardascension.util;

import youss3f.unaux.com.shardascension.ability.AbilityManager;
import youss3f.unaux.com.shardascension.ability.combined.FlameBarrageAbility;
import youss3f.unaux.com.shardascension.ability.combined.FrozenPullAbility;
import youss3f.unaux.com.shardascension.ability.combined.SonicDashAbility;
import youss3f.unaux.com.shardascension.ability.combined.ThunderstormAbility;
import youss3f.unaux.com.shardascension.ability.combined.VampiricSurgeAbility;
import youss3f.unaux.com.shardascension.ability.standard.AscensionSurgeAbility;
import youss3f.unaux.com.shardascension.ability.standard.BlinkAbility;
import youss3f.unaux.com.shardascension.ability.standard.CrystalBarrageAbility;
import youss3f.unaux.com.shardascension.ability.standard.DashAbility;
import youss3f.unaux.com.shardascension.ability.standard.EarthSlamAbility;
import youss3f.unaux.com.shardascension.ability.standard.FlameWaveAbility;
import youss3f.unaux.com.shardascension.ability.standard.GravityWellAbility;
import youss3f.unaux.com.shardascension.ability.standard.IcePrisonAbility;
import youss3f.unaux.com.shardascension.ability.standard.LifeStealAbility;
import youss3f.unaux.com.shardascension.ability.standard.PhoenixRiseAbility;
import youss3f.unaux.com.shardascension.ability.standard.RadiantHealAbility;
import youss3f.unaux.com.shardascension.ability.standard.ShadowStepAbility;
import youss3f.unaux.com.shardascension.ability.standard.ShardArmorAbility;
import youss3f.unaux.com.shardascension.ability.standard.ShardBurstAbility;
import youss3f.unaux.com.shardascension.ability.standard.ShardRainAbility;
import youss3f.unaux.com.shardascension.ability.standard.ShockwaveAbility;
import youss3f.unaux.com.shardascension.ability.standard.ThunderStrikeAbility;
import youss3f.unaux.com.shardascension.ability.standard.TimeBurstAbility;
import youss3f.unaux.com.shardascension.ability.standard.VoidPullAbility;
import youss3f.unaux.com.shardascension.ability.standard.WindStepAbility;

public final class AbilityRegistrar {

    private AbilityRegistrar() {
    }

    public static void registerAll(AbilityManager manager) {
        manager.registerAbility(new DashAbility());
        manager.registerAbility(new BlinkAbility());
        manager.registerAbility(new ShardBurstAbility());
        manager.registerAbility(new ShardRainAbility());
        manager.registerAbility(new ShockwaveAbility());
        manager.registerAbility(new WindStepAbility());
        manager.registerAbility(new LifeStealAbility());
        manager.registerAbility(new FlameWaveAbility());
        manager.registerAbility(new IcePrisonAbility());
        manager.registerAbility(new ThunderStrikeAbility());
        manager.registerAbility(new VoidPullAbility());
        manager.registerAbility(new ShadowStepAbility());
        manager.registerAbility(new EarthSlamAbility());
        manager.registerAbility(new ShardArmorAbility());
        manager.registerAbility(new RadiantHealAbility());
        manager.registerAbility(new TimeBurstAbility());
        manager.registerAbility(new GravityWellAbility());
        manager.registerAbility(new CrystalBarrageAbility());
        manager.registerAbility(new PhoenixRiseAbility());
        manager.registerAbility(new AscensionSurgeAbility());

        manager.registerAbility(new SonicDashAbility());
        manager.registerAbility(new ThunderstormAbility());
        manager.registerAbility(new FlameBarrageAbility());
        manager.registerAbility(new VampiricSurgeAbility());
        manager.registerAbility(new FrozenPullAbility());

        manager.registerCombination("dash", "wind_step", "sonic_dash");
        manager.registerCombination("thunder_strike", "shard_burst", "thunderstorm");
        manager.registerCombination("flame_wave", "crystal_barrage", "flame_barrage");
        manager.registerCombination("life_steal", "radiant_heal", "vampiric_surge");
        manager.registerCombination("ice_prison", "gravity_well", "frozen_pull");
    }
}
