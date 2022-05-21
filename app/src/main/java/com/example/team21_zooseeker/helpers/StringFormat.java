package com.example.team21_zooseeker.helpers;

import android.content.Context;

import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.activities.directions.DirectionItem;
import com.example.team21_zooseeker.activities.route.IdentifiedWeightedEdge;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StringFormat {

    public Graph<String, IdentifiedWeightedEdge> g;
    public Map<String, ZooData.VertexInfo> vInfo;
    public Map<String, ZooData.EdgeInfo> eInfo;


    public StringFormat(Context context){
        // 1. Load the graph...
        g = ZooData.loadZooGraphJSON(context, context.getString(R.string.ZOO_GRAPH));

        // 2. Load the information about our nodes and edges...
        vInfo = ZooData.loadVertexInfoJSON(context, context.getString(R.string.NODE_INFO));
        eInfo = ZooData.loadEdgeInfoJSON(context, context.getString(R.string.EDGE_INFO));
    }

    /**
     * getDirections
     *
     * @param route The List of GraphPaths that constitute the calculated route
     * @param verbose True for detailed directions, false for brief
     * @return
     */
    public List<DirectionItem> getDirections(List<GraphPath<String, IdentifiedWeightedEdge>> route, boolean verbose){
        List<DirectionItem> directions = new ArrayList<DirectionItem>();
        List<String> directionsStr = new ArrayList<String>();

        for(GraphPath<String, IdentifiedWeightedEdge> path : route) {
            int count = 1;
            List<String> vertices = path.getVertexList();
            int size = vertices.size();
            String str = "";
            String name = "";
            IdentifiedWeightedEdge previous = null;
            double totalWeight = 0;
            boolean condensed = true;
            for (int i = 0; i < size - 1; i++) {
                IdentifiedWeightedEdge e = g.getEdge(vertices.get(i), vertices.get(i + 1));
                String dirs = "";
                if (verbose) {
                    if (previous == null || !(eInfo.get(e.getId()).street.equals(eInfo.get(previous.getId()).street))) {
                        dirs += String.format("%d. Proceed on %s %d ft to %s.\n",
                                count,
                                eInfo.get(e.getId()).street,
                                (int) (g.getEdgeWeight(e)),
                                vInfo.get(vertices.get(i + 1)).name);
                    } else {
                        dirs += String.format("%d. Continue on %s %d ft to %s.\n",
                                count,
                                eInfo.get(e.getId()).street,
                                (int) (g.getEdgeWeight(e)),
                                vInfo.get(vertices.get(i + 1)).name);
                    }
                    count++;
                    previous = e;
                }
                else {
                    if (previous == null || eInfo.get(e.getId()).street.equals(eInfo.get(previous.getId()).street)) {
                        condensed = false;
                        totalWeight += g.getEdgeWeight(e);
                    }

                    else{
                        condensed = true;
                        dirs += String.format("%d. Proceed on %s %d ft to %s.\n",
                                count,
                                eInfo.get(previous.getId()).street,
                                (int) totalWeight,
                                vInfo.get(vertices.get(i)).name);

                        totalWeight = g.getEdgeWeight(e);
                        count++;
                    }
                    previous = e;
                }
                if(condensed) {
                    str += dirs + "\n";
                    dirs = "";
                }

                //have to add ending string since we needed to look ahead
                if(i == size-2){
                    dirs = String.format("%d. Proceed on %s %d ft to %s.",
                            count,
                            eInfo.get(previous.getId()).street,
                            (int) totalWeight,
                            vInfo.get(vertices.get(i+1)).name);
                }
                str += dirs;
                name = vInfo.get(vertices.get(i+1)).name;

            }
            directionsStr.add(str);
            directions.add(new DirectionItem(name, str));
        }
        for(String s : directionsStr){
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println(s);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
        return directions;
    }

    /**
     * initialDirections
     *
     * @param route The List of GraphPaths that constitute the calculated route
     * @return List of formatted strings to display directly after hitting 'plan'
     *
     * Each String in the returned list is formatted as ExhibitName, [number]ft
     * ex, "Alligators, 120ft"
     *
     *  Calls: calculateRoute
     */
    public List<String> initialDirections(List<GraphPath<String, IdentifiedWeightedEdge>> route){

        List<String> dispStrings = new ArrayList<String>();

        //We accumulate the total weight of the paths as we go
        double weight = 0;
        for(GraphPath<String, IdentifiedWeightedEdge> path : route){
            weight += path.getWeight();
            String exhibit = vInfo.get(path.getEndVertex()).name;

            dispStrings.add(exhibit + ", " + (int)weight + "ft");
        }
        return dispStrings;
    }



    /**
     * printDebugInfo
     *
     * @param route graph-path list generated by calculateRoute
     *
     * Prints out the graph-path list, with node names, proper to-from direction,
     * and distance. Entries in the list are clearly demarcated in the output.
     * Calls to System.out.print are put in logcat.Debug
     */
    public void printDebugInfo(List<GraphPath<String, IdentifiedWeightedEdge>> route){
        for(GraphPath<String, IdentifiedWeightedEdge> path : route){
            int count = 1;

            List<String> vertices = path.getVertexList();
            int size = vertices.size();
            String str = "";
            for(int i = 0; i < size-1; i++){
                IdentifiedWeightedEdge e = g.getEdge(vertices.get(i), vertices.get(i+1));
                str += String.format("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
                        count,
                        g.getEdgeWeight(e),
                        eInfo.get(e.getId()).street,
                        vInfo.get(vertices.get(i)).name,
                        vInfo.get(vertices.get(i+1)).name);
                count++;
            }
            System.out.println(str);
            System.out.printf("-------------------------------------------\n");
        }
    }
}
