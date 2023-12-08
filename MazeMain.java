import java.util.Random;
import java.util.Scanner;

class Agent{
   int posX;
   int posY;
   Agent(int posX, int posY){
      this.posX = posX;
      this.posY = posY;
   }

   void goUp(Maze maze){
      if((posY-1<0) || (maze.maze[posY-1][posX] == 1)){
         System.out.println("You cannnot go up.");
      }else{
         maze.removeAgent(this);
         posY -= 1;
         maze.putAgent(this);
      }
   }
   void goDown(Maze maze){
      if((posY+1>maze.height-1) || (maze.maze[posY+1][posX] == 1)){
         System.out.println("You cannnot go down.");
      }else{
         maze.removeAgent(this);
         posY += 1;
         maze.putAgent(this);
      }
   }
   void goLeft(Maze maze){
      if((posX-1<0) || (maze.maze[posY][posX-1] == 1)){
         System.out.println("You cannnot go left.");
      }else{
         maze.removeAgent(this);
         posX -= 1;
         maze.putAgent(this);
      }
   }
   void goRight(Maze maze){
      if((posX+1>maze.width-1) || (maze.maze[posY][posX+1] == 1)){
         System.out.println("You cannnot go right.");
      }else{
         maze.removeAgent(this);
         posX += 1;
         maze.putAgent(this);
      }
   }

}

class Maze{
   int[][] maze;
   int height;
   int width;
   Maze(int height, int width){
      this.height = height;
      this.width = width;
      this.maze = createMaze(height, width);
   }
   
   void digMaze(int yp, int xp, int iterationCount){
      int iterationLimit = 10 * 10 * 10 * 2;
      if(iterationCount < iterationLimit){
         Random rand = new Random();
         //2つ先までのベクトル
         //上、下、左、右
         int[][] vy = {{-1,-2}, {1,2}, {0,0}, {0,0}};
         int[][] vx = {{0,0}, {0,0}, {-1,-2}, {1,2}};

         //方向を選択 0:上 1:下 2: 左 3: 右
         int[] directions = {0, 1, 2, 3};
         //シャッフル
         for (int i = 3; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            int tmp = directions[index];
            directions[index] = directions[i];
            directions[i] = tmp;
         }

         for (int direction: directions){//ランダムな順番の方向で
            boolean con1 = !(xp+vx[direction][1]<0 || xp+vx[direction][1]>width-1); //横方向 壁を超えていない
            boolean con2 = !(yp+vy[direction][1]<0 || yp+vy[direction][1]>height-1); //縦方向 壁を超えていない
            boolean con3 = false;
            if(con1 && con2){
               con3 = maze[yp+vy[direction][1]][xp+vx[direction][1]] != 0; //行き先がすでに空白でない
            }

            if(con1 && con2 && con3){//条件が揃っている
               for (int i = 0; i < 2; i++) {
                  maze[yp+vy[direction][i]][xp+vx[direction][i]] = 0;//2マス掘る
               }
               digMaze(yp+vy[direction][1], xp+vx[direction][1], iterationCount+1);//堀った先に移動
            }
         }
      }
   }

   
   int[][] createMaze(int height, int width){
      //ベースの生成
      int[][] maze = new int[height][width];
      
      for (int i = 0; i < height; i++) {
         for (int j = 0; j < width; j++) {
            maze[i][j] = 1;
         }
      }

      //スタート地点
      maze[0][0]=0;

      return maze;
   }   
   
   //迷路のエージェント設置
   void putAgent(Agent agent){
      maze[agent.posY][agent.posX] = 2;
   }

   //迷路のエージェント削除
   void removeAgent(Agent agent){
      maze[agent.posY][agent.posX] = 0;
   }

   //標準出力用
   @Override
   public String toString(){
      String result = "";
      for (int i = 0; i < maze[0].length+2; i++) {
         result += "■";
      }
      result += "\n";
      for (int i = 0; i < maze.length; i++) {
         result += "■";
         for (int j = 0; j < maze[0].length; j++) {
            if(maze[i][j]==0){
               if(i==maze.length-1 && j ==maze[0].length-1){
                  result += "G";
               }else{
                  result += " ";
               }
            }else if(maze[i][j]==2){
               result += "○";
            }else{
               result += "■";
            }
         }
         result += "■";
         if(i==0){
            result += "  |'w': go up";
         }else if(i==1){
            result += "  |'a': go left";
         }else if(i==2){
            result += "  |'s': go down";
         }else if(i==3){
            result += "  |'d': go right";
         }
         result += "\n";
      }
      for (int i = 0; i < maze[0].length+2; i++) {
         result += "■";
      }
      return result;
   }
}

public class MazeMain {
   public static void main(String[] args){
      int height = 7;
      int width = 7;
      //エージェント生成
      Agent agent = new Agent(0, 0);
      //迷路生成
      Maze maze = new Maze(height, width);
      Scanner stdIn = new Scanner(System.in);
      maze.digMaze(0, 0, 0);
      maze.putAgent(agent);

      //ゲーム
      boolean clear = false;
      while(!clear){
         System.out.println("MAZE GAME");
         System.out.println(maze);
         if(agent.posX == width-1 && agent.posY == height-1){
            clear = true;
            break;
         }
         System.out.print("direction?");
         char input = stdIn.nextLine().charAt(0);
         if(input == 'w'){
            agent.goUp(maze);
         }else if(input == 's'){
            agent.goDown(maze);
         }else if(input == 'a'){
            agent.goLeft(maze);
         }else if(input == 'd'){
            agent.goRight(maze);
         }else{
            System.out.println("\nPlease enter 'w', 'a', 's' or 'd'.");
         }
      }
      System.out.println("CLEAR!!");
      
      stdIn.close();
   }
}
