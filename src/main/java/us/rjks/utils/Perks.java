package us.rjks.utils;

import org.bukkit.Material;
import us.rjks.game.Main;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 19.05.2021 / 19:02
 *
 **************************************************************************/

public class Perks {

    public void loadPerks() {

    }

    public class Perk {

        private String name, ingame, category;
        private Material replacement, type;
        private Integer price, damage;
        private boolean alreadyBought;

        public Perk(String name, String ingame, String category, Material replacement, Material type, Integer price, Integer damage, boolean alreadyBought) {
            this.name = name;
            this.ingame = ingame;
            this.category = category;
            this.replacement = replacement;
            this.type = type;
            this.price = price;
            this.damage = damage;
            this.alreadyBought = alreadyBought;

        }

        public boolean playerHasPerk(String uuid) {
            if (alreadyBought) return true;
            return Main.getGame().getShop().hasItem(uuid, name);
        }

        public boolean selectPerk(String uuid) {
            if (!playerHasPerk(uuid)) return false;

            //TODO: SELECT PERK IN DATABASE

            return true;
        }

        public Material getType() {
            return type;
        }

        public Integer getDamage() {
            return damage;
        }

        public Integer getPrice() {
            return price;
        }

        public Material getReplacement() {
            return replacement;
        }

        public String getCategory() {
            return category;
        }

        public String getIngame() {
            return ingame;
        }

        public String getName() {
            return name;
        }
    }

    public void create() {

    }

}
