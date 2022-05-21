package com.example.team21_zooseeker.activities.route;

import android.content.Context;

import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.helpers.StringFormat;
import com.example.team21_zooseeker.helpers.ZooData;
import com.example.team21_zooseeker.activities.directions.DirectionItem;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteCalc {
    private Graph<String, IdentifiedWeightedEdge> g;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private Context context;

    public RouteCalc(Context context){
        this.context = context;

        // 1. Load the graph...
        g = ZooData.loadZooGraphJSON(context, context.getString(R.string.ZOO_GRAPH));

        // 2. Load the information about our nodes and edges...
        vInfo = ZooData.loadVertexInfoJSON(context, context.getString(R.string.NODE_INFO));
        eInfo = ZooData.loadEdgeInfoJSON(context, context.getString(R.string.EDGE_INFO));
    }


    /**
     * calculateRoute
     *
     * @param start Starting location of each route
     * @param exhibits The users selection of exhibits to see
     * @return A list of graph-paths outlining an efficient route to see
     * each exhibit once, then return back to the start
     *
     * Calls: findNextClosestExhibit
     */
    public List<GraphPath<String, IdentifiedWeightedEdge>> calculateRoute(String start, List<String> exhibits){
        List<GraphPath<String, IdentifiedWeightedEdge>> route =
                new ArrayList<GraphPath<String, IdentifiedWeightedEdge>>();

        //Tells us where we currently are in the route
        String current = start;

        //will become path from current to next vertex
        GraphPath<String, IdentifiedWeightedEdge> nextPath = null;

        while((nextPath = findNextClosestExhibit(current, exhibits)) != null){
            route.add(nextPath);
            current = vInfo.get(nextPath.getEndVertex()).id; //current updated to reflect location at end of path
            exhibits.remove(current); //since we are now at current, it has been visited and can be removed from the list
        }

        //We need to add one last path, from our current location back to the start
        route.add(DijkstraShortestPath.findPathBetween(g, current, context.getString(R.string.ENTRANCE_EXIT)));

        //check logcat.D to see whats going on
        //printDebugInfo(route);
        //getDirections(route, false);

        return route;
    }

    /**
     * findNextClosestExhibit
     *
     * @param current The vertex we are currently at
     * @param exhibits A list of exhibits we have yet to visit (NOT the user selection list)
     * @return A graph-path from current, to the next closest node in exhibits
     */
    GraphPath<String, IdentifiedWeightedEdge> findNextClosestExhibit(String current, List<String> exhibits){
        //Classic find min, we are finding min weight from current to each exhibit in the list
        double minWeight = Double.MAX_VALUE;
        GraphPath<String, IdentifiedWeightedEdge> nextPath = null;
        for(String exhibit : exhibits) {

            GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, current, exhibit);
            if(path.getWeight() < minWeight){
                minWeight = path.getWeight();
                nextPath = path;
            }
        }
        return nextPath;
    }

    /**
     * singleShortestPath
     * @param start Node to start at
     * @param goal Node to get to
     */
    GraphPath<String, IdentifiedWeightedEdge> singleShortestPath(String start, String goal){
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, goal);
        return path;
    }
}
