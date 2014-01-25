package bee.vindinium.main;

import java.util.ArrayList;
import java.util.List;

public class BeeBot1 implements Bot {

	BFS bfs = null;

	@Override
	public Direction nextMove(State state) {

		Hero me = state.hero();
		Hero ennemi = anotherHero(state);

		int[] start = heroToIntArray(me);
		int[] end = heroToIntArray(ennemi);

		System.out.println(Utils.positionToString(start));
		System.out.println(Utils.positionToString(end));

//		Utils.printMaze(Utils.buildMaze(state));
		
//		BFS bfs = new BFS(state, start, new int[] {0,2});
//		ArrayList<int[]> chemin = bfs.getDistance(start, new int[] {0,2});
		BFS bfs = new BFS(state.game.board, start, end);
		ArrayList<int[]> chemin = bfs.getDistance();

		Utils.printList("chemin : ", chemin);
		if (chemin.size() <= 1)
			return Direction.STAY;

		int[] next = chemin.get(1);

		System.out.print("ennemi: " + ennemi.id + " ");
		System.out.print("start: " + start + " ");
		System.out.print("end: " + end + " ");
		System.out.println("next: " + next);

		
		
		return Utils.direction(start, next);

	}

	private Hero anotherHero(State state) {
		List<Hero> l = state.game.heroes;
		for (Hero r : l)
			if (r != state.hero())
				return r;
		return null;
	}

	private int[] heroToIntArray(Hero hero) {
		int x = hero.position.getKey().intValue();
		int y = hero.position.getValue().intValue();
		return new int[] { x, y };
	}

}
