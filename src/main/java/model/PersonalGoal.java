package model;

public enum PersonalGoal {

    GOAL_1(
            new TileGoal[]{
                    new TileGoal(Tile.CATS, 0, 0),
                    new TileGoal(Tile.FRAMES, 1, 3),
                    new TileGoal(Tile.PLANTS, 2, 1),
                    new TileGoal(Tile.TROPHIES, 3, 0),
                    new TileGoal(Tile.GAMES, 4, 4),
                    new TileGoal(Tile.BOOKS, 5, 2)
            }
    ),
    GOAL_2(
            new TileGoal[]{
                    new TileGoal(Tile.GAMES, 0, 4),
                    new TileGoal(Tile.TROPHIES, 2, 0),
                    new TileGoal(Tile.FRAMES, 2, 2),
                    new TileGoal(Tile.PLANTS, 3, 3),
                    new TileGoal(Tile.BOOKS, 4, 1),
                    new TileGoal(Tile.CATS, 4, 2)
            }
    ),
    GOAL_3(
            new TileGoal[]{
                    new TileGoal(Tile.TROPHIES, 1, 1),
                    new TileGoal(Tile.FRAMES, 3, 1),
                    new TileGoal(Tile.BOOKS, 3, 2),
                    new TileGoal(Tile.PLANTS, 4, 4),
                    new TileGoal(Tile.GAMES, 5, 0),
                    new TileGoal(Tile.CATS, 5, 3)
            }
    ),
    GOAL_4(
            new TileGoal[]{
                    new TileGoal(Tile.TROPHIES, 0, 2),
                    new TileGoal(Tile.CATS, 0, 4),
                    new TileGoal(Tile.BOOKS, 2, 3),
                    new TileGoal(Tile.GAMES, 4, 1),
                    new TileGoal(Tile.FRAMES, 4, 2),
                    new TileGoal(Tile.PLANTS, 5, 0)
            }
    ),
    GOAL_5(
            new TileGoal[]{
                    new TileGoal(Tile.PLANTS, 0, 2),
                    new TileGoal(Tile.BOOKS, 1, 1),
                    new TileGoal(Tile.GAMES, 2, 0),
                    new TileGoal(Tile.FRAMES, 3, 2),
                    new TileGoal(Tile.CATS, 4, 4),
                    new TileGoal(Tile.TROPHIES, 5, 3)
            }
    ),
    GOAL_6(
            new TileGoal[]{
                    new TileGoal(Tile.TROPHIES, 0, 4),
                    new TileGoal(Tile.GAMES, 1, 1),
                    new TileGoal(Tile.BOOKS, 2, 0),
                    new TileGoal(Tile.CATS, 3, 3),
                    new TileGoal(Tile.FRAMES, 4, 1),
                    new TileGoal(Tile.PLANTS, 4, 3)
            }
    ),
    GOAL_7(
            new TileGoal[]{
                    new TileGoal(Tile.FRAMES, 0, 4),
                    new TileGoal(Tile.CATS, 1, 1),
                    new TileGoal(Tile.TROPHIES, 2, 2),
                    new TileGoal(Tile.PLANTS, 3, 0),
                    new TileGoal(Tile.BOOKS, 4, 3),
                    new TileGoal(Tile.GAMES, 5, 3)
            }
    ),
    GOAL_8(
            new TileGoal[]{
                    new TileGoal(Tile.GAMES, 0, 2),
                    new TileGoal(Tile.CATS, 2, 2),
                    new TileGoal(Tile.BOOKS, 3, 4),
                    new TileGoal(Tile.TROPHIES, 4, 1),
                    new TileGoal(Tile.PLANTS, 4, 4),
                    new TileGoal(Tile.FRAMES, 5, 0)
            }
    ),
    GOAL_9(
            new TileGoal[]{
                    new TileGoal(Tile.PLANTS, 1, 1),
                    new TileGoal(Tile.CATS, 2, 0),
                    new TileGoal(Tile.GAMES, 2, 2),
                    new TileGoal(Tile.BOOKS, 3, 4),
                    new TileGoal(Tile.TROPHIES, 4, 3),
                    new TileGoal(Tile.FRAMES, 5, 4)
            }
    ),
    GOAL_10(
            new TileGoal[]{
                    new TileGoal(Tile.PLANTS, 0, 0),
                    new TileGoal(Tile.FRAMES, 0, 2),
                    new TileGoal(Tile.CATS, 1, 4),
                    new TileGoal(Tile.BOOKS, 2, 3),
                    new TileGoal(Tile.GAMES, 3, 1),
                    new TileGoal(Tile.TROPHIES, 5, 2)
            }
    ),
    GOAL_11(
            new TileGoal[]{
                    new TileGoal(Tile.BOOKS, 0, 2),
                    new TileGoal(Tile.PLANTS, 1, 1),
                    new TileGoal(Tile.FRAMES, 2, 2),
                    new TileGoal(Tile.TROPHIES, 3, 3),
                    new TileGoal(Tile.GAMES, 4, 4),
                    new TileGoal(Tile.CATS, 5, 0)
            }
    ),
    GOAL_12(
            new TileGoal[]{
                    new TileGoal(Tile.FRAMES, 1, 0),
                    new TileGoal(Tile.GAMES, 1, 3),
                    new TileGoal(Tile.PLANTS, 2, 2),
                    new TileGoal(Tile.CATS, 3, 1),
                    new TileGoal(Tile.TROPHIES, 3, 4),
                    new TileGoal(Tile.BOOKS, 5, 0)
            }
    );


    private TileGoal[] tileGoals;

    PersonalGoal(TileGoal[] goals) {
        this.tileGoals = goals;
    }

    public TileGoal[] getTileGoals() {
        return tileGoals.clone();
    }

}
