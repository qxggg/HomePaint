package com.homepainter.service;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;




@Service
public class SpliteHouseService {

    @Autowired
    UltraGCN ultraGCN;

    public List<Map<String,Object>> WallPoints;
    public List<Map<String,Object>> Walls;

    public int n = 0;

    public int [][]visited;

    public int [][]adj_matrix ;
    public double []num_adjacent = new double[10000];

    public int now_num_adjacent = 0;

    public List<List<Integer>> closed_spaces = new ArrayList<>();

    /**
     * 户型图 分房算法 总入口
     * @param json 识别出来的结果
     * @return
     * @throws Exception
     */
    public Map<String,Object> SpliteHouseController(Map<String,Object> json,int userId) throws Exception {

        Map<String,Object> temp =  json;

        temp = (Map<String, Object>) temp.get("data");

        // 分房算法
        WallPoints = (List<Map<String, Object>>) temp.get("WallPoints");
        Walls = (List<Map<String, Object>>) temp.get("Walls");


        start();

        adj_matrix = remove_degree_one_nodes(adj_matrix);

        find_closed_spaces();

        closed_spaces = remove2(closed_spaces);


        // 获取每个房间的面积
        List<Map<String,Object>> RoomArea = getAllHouse();
        // 最大的为整个房间的面积
        Map<String,Object> house = RoomArea.get(0);
        RoomArea.remove(0);
        // 获取房间名称
        RoomArea = getAllRoomName(RoomArea);
        // 获取风格
        String style = ultraGCN.GetUserStyle(userId);


        Map<String,Object> res = new HashMap<>();
        res.put("code",0);
        res.put("Room",RoomArea);
        res.put("House",house);
        res.put("size",closed_spaces.size());
        res.put("WallHeight",2.8);
        res.put("style",style);
        return res;
    }

    /**
     * 计算每个房间的名称
     * @param rooms
     * @return
     */
    public List<Map<String,Object>>  getAllRoomName(List<Map<String,Object>> rooms){
        rooms.get(0).put("name","客厅");

        for(int i=1;i<rooms.size();i++){
            double area = Double.parseDouble( rooms.get(i).get("area").toString() );
            if(area<=5){
                rooms.get(i).put("name","卫生间");
            }else{
                rooms.get(i).put("name","卧室");
            }

        }



        return rooms;
    }

    public void start(){
        n = WallPoints.size();
        visited = new int[n][n];
        adj_matrix = new int[n][n];

        for(int i=0;i< Walls.size();i++){
            int start_point = (int) Walls.get(i).get("start_point");
            int end_point = (int) Walls.get(i).get("end_point");

            adj_matrix[start_point][end_point] = 1;
            adj_matrix[end_point][start_point] = 1;
        }

        for (int i = 0; i < adj_matrix.length; i++) {
            int sum = 0;
            for (int j = 0; j < adj_matrix[i].length; j++) {
                sum += adj_matrix[i][j];
            }
            num_adjacent[now_num_adjacent++]=sum;
        }



    }

    public static int[][] remove_degree_one_nodes(int[][] adjMatrix) {
        int n = adjMatrix.length;
        int[] nodes = new int[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = i;
        }
        int[] degrees = new int[n];
        for (int i = 0; i < n; i++) {
            int degree = 0;
            for (int j = 0; j < n; j++) {
                degree += adjMatrix[i][j];
            }
            degrees[i] = degree;
        }
        List<Integer> toRemove = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            if (degrees[i] == 1) {
                toRemove.add(i);
            }
        }
        while (!toRemove.isEmpty()) {
            int node = toRemove.remove(toRemove.size() - 1);
            if (contains(nodes, node)) {
                degrees[node] -= 1;
                for (int neighbor = 0; neighbor < n; neighbor++) {
                    if (adjMatrix[node][neighbor] == 1) {
                        degrees[neighbor] -= 1;
                        if (degrees[neighbor] == 1) {
                            toRemove.add(neighbor);
                        }
                        adjMatrix[node][neighbor] = 0;
                        adjMatrix[neighbor][node] = 0;
                    }
                }
            }
        }
        int[][] newAdjMatrix = new int[nodes.length][nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes.length; j++) {
                newAdjMatrix[i][j] = adjMatrix[nodes[i]][nodes[j]];
            }
        }
        return newAdjMatrix;
    }

    private static boolean contains(int[] arr, int val) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == val) {
                return true;
            }
        }
        return false;
    }

    public static double calcAngle(double[] v1, double[] v2) {
        double v1Norm = Math.sqrt(v1[0] * v1[0] + v1[1] * v1[1] );
        double v2Norm = Math.sqrt(v2[0] * v2[0] + v2[1] * v2[1] );
        double dotProduct = v1[0] * v2[0] + v1[1] * v2[1] ;
        double r = Math.acos(dotProduct / (v1Norm * v2Norm));
        double deg = r * 180 / Math.PI;

        double[] a1 = { v1[0], v1[1], 0 };
        double[] a2 = { v2[0], v2[1], 0 };
        double[] a3 = crossProduct(a1, a2);

        if (Math.signum(a3[2]) > 0 && deg != 360) {
            deg = 360 - deg;
        }

        return deg;
    }

    private static double[] crossProduct(double[] v1, double[] v2) {
        double[] result = new double[3];
        result[0] = v1[1] * v2[2] - v1[2] * v2[1];
        result[1] = v1[2] * v2[0] - v1[0] * v2[2];
        result[2] = v1[0] * v2[1] - v1[1] * v2[0];
        return result;
    }

    public void dfs(int node, List<Integer> path, int num) {
        if (num != 0) {
            int minn = 10; //最小夹角点下标
            int minarc = 10000;
            double[] last_vec = { (Double.parseDouble( WallPoints.get(path.get(path.size()-2)).get("x").toString() ) - Double.parseDouble( WallPoints.get(node).get("x").toString() )),  (Double.parseDouble( WallPoints.get(path.get(path.size()-2)).get("y").toString() ) - Double.parseDouble( WallPoints.get(node).get("y").toString()))};

            //计算顺时针角度最小
            for (int i = 0; i < n; i++) {
                if (adj_matrix[node][i] == 1) {
                    double[] now_vec = { Double.parseDouble( WallPoints.get(i).get("x").toString() ) - Double.parseDouble( WallPoints.get(node).get("x").toString() ),Double.parseDouble( WallPoints.get(i).get("y").toString() ) - Double.parseDouble( WallPoints.get(node).get("y").toString() )};
                    double alpha = calcAngle(last_vec, now_vec);

                    if (minarc > alpha && alpha != 0 && num_adjacent[i] > 1) {
                        minarc = (int) alpha;
                        minn = i;
                    }
                }
            }
            //当前方向上的这条边没被走过
            if (visited[node][minn] == 0) {

                visited[node][minn] += 1;
                if (path.contains(minn)) {

                    closed_spaces.add(path.subList(path.indexOf(minn), path.size()));

                } else {
                    List<Integer> newPath = new ArrayList<>(path);
                    newPath.add(minn);
                    dfs(minn, newPath, num+1);
                }
            }

        } else {
            for (int i = 0; i < n; i++) {
                if (adj_matrix[node][i] == 1) {
                    if (visited[node][i] == 0) {
                        visited[node][i] += 1;

                        if (path.contains(i)) {
                            closed_spaces.add(path.subList(path.indexOf(i), path.size()));

                        } else {
                            List<Integer> newPath = new ArrayList<>(path);
                            newPath.add(i);
                            dfs(i, newPath, num+1);
                        }
                    }
                }
            }
        }
    }

    public void find_closed_spaces(){
        for(int i=0;i<n;i++){{
            if(num_adjacent[i]>1){
                List<Integer> path = new ArrayList<>();
                path.add(i);

                dfs(i,path,0);
            }

        }}
    }

    public List<List<Integer>> remove2(List<List<Integer>> closed_list) {
        // 筛选去除长度为2的闭环
        int n = closed_list.size();
        int num = 0;
        for (int i = 0; i < n; i++) {
            if (closed_list.get(num).size() == 2) {
                closed_list.remove(num);
            } else {
                num += 1;
            }

            if (num == closed_list.size()) {
                break;
            }
        }
        return closed_list;
    }

    public List<Map<String,Object>> getAllHouse(){
        List<Map<String,Object>> res = new ArrayList<>();

        for(int i=0;i< closed_spaces.size();i++){
            Map<String,Object> temp = new HashMap<>();
            List<Map<String,Object>> point = new ArrayList<>();
            int m = closed_spaces.get(i).size();
            double[][] vertices = new double[m][2];
            double sumx = 0;
            double sumy = 0;
            for(int j=0;j< m;j++){
                int id = closed_spaces.get(i).get(j);
                vertices[j][0] = getWallsPointX(id);
                vertices[j][1] = getWallsPointY(id);
                // 整理返回结果
                Map<String,Object> tempp = new HashMap<>();
                tempp.put("x",vertices[j][0]);
                tempp.put("y",vertices[j][1]);
                tempp.put("id",id);
                point.add(tempp);
                // 计算均值
                sumx += vertices[j][0];
                sumy += vertices[j][1];
            }

            Map<String,Object> center = new HashMap<>();
            center.put("x",sumx/m);
            center.put("y",sumy/m);

            double area = getArea(vertices);
            temp.put("area",area);
            temp.put("point",point);
            temp.put("center",center);
            res.add(temp);
        }

        Collections.sort(res, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return Double.compare( Double.parseDouble( o2.get("area").toString() ),Double.parseDouble( o1.get("area").toString() ));
            }
        });

        return res;
    }

    public double getWallsPointX(int target){
        for(int i=0;i< WallPoints.size();i++){

            int id = (int)WallPoints.get(i).get("id");
            if(id==target){
                return Double.parseDouble( WallPoints.get(i).get("x").toString() );
            }
        }
        return -1;
    }

    public double getWallsPointY(int target){
        for(int i=0;i< WallPoints.size();i++){

            int id = (int)WallPoints.get(i).get("id");
            if(id==target){
                return Double.parseDouble( WallPoints.get(i).get("y").toString() );
            }
        }
        return -1;
    }

    /**
     * 计算 矩阵 顶点 组成的多边形的面积
     * @param matrix
     * @return
     */
    public double getArea(double[][] matrix) {
        double area = 0.0;
        int n = matrix.length;

        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            area += matrix[i][0] * matrix[j][1];
            area -= matrix[i][1] * matrix[j][0];
        }

        area /= 2.0;
        return Math.abs(area);
    }


}
