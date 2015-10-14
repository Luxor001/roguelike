DROP TABLE IF EXISTS "Enemies";
CREATE TABLE "Enemies" ("Id" INTEGER PRIMARY KEY  NOT NULL ,"Type" TEXT NOT NULL ,"Name" TEXT NOT NULL ,"Description" TEXT,"HpMax" INTEGER NOT NULL ,"HpMin" INTEGER NOT NULL  DEFAULT (null) ,"AttackMax" FLOAT NOT NULL ,"AttackMin" FLOAT NOT NULL ,"DefenceMax" FLOAT NOT NULL ,"DefenceMin" FLOAT NOT NULL ,"SpeedMax" FLOAT NOT NULL ,"SpeedMin" FLOAT NOT NULL ,"SightMax" FLOAT NOT NULL ,"SightMin" FLOAT NOT NULL ,"SpritesheetPath" TEXT NOT NULL );
INSERT INTO "Enemies" VALUES(1,'SKELETON','Crypt Skeleton','A pile of bones',10,6,5,3,7,5,7,5,7,4,'creatures/skeleton_spritesheetx32.png');
INSERT INTO "Enemies" VALUES(2,'ORC','Cave Orc','It''s ugly as hell!!',12,8,6,4,6,4,5,3,7,4,'creatures/skeleton_spritesheetx32.png');
INSERT INTO "Enemies" VALUES(3,'UGLYYETI','Ugly Beast','His parents aren''t proud',12,8,6,4,6,4,5,3,7,4,'creatures/wtfCreature2.png');
INSERT INTO "Enemies" VALUES(4,'WRAITH','Dark Shade','Dark as barman.',12,8,6,4,6,4,5,3,7,4,'creatures/wraith2.png');
