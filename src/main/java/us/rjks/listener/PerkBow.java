package us.rjks.listener;

import org.bukkit.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import us.rjks.game.Main;
import us.rjks.utils.Perks;

import javax.annotation.Nonnull;
import java.util.List;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 24.05.2021 / 12:24
 *
 **************************************************************************/

public class PerkBow implements Listener {

    @EventHandler
    public void onShot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && event.getProjectile() instanceof Projectile) {
            Perks.Perk perk = Perks.getPerkFromName(Main.getGame().getShop().getSelectedPerk(((Player) event.getEntity()).getUniqueId().toString(), "PARTICLE"));
            if (perk.getDataValue("particle") == null) return;
            Main.getGame().getProjectiles().put((Projectile)event.getProjectile(), perk.getDataValue("particle").toString());
        }
    }

    @EventHandler
    public void onArrow(ProjectileHitEvent event) {
        if(event.getEntity() instanceof Arrow){
            Main.getGame().getProjectiles().remove(event.getEntity());
        }
    }

}
