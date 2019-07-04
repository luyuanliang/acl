package a;

/**
 * Created by luyl on 17-11-21.
 */
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    @Getter
    @Setter
    static class Node {

        private String key;
        private List<String> childrenTokens;
        private List<Node> children;

        public Node(String key) {
            this.key = key;
            children = Lists.newArrayList();
            childrenTokens = Lists.newArrayList();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Node && ((Node) obj).key.equals(this.key);
        }
    }

    public static void main(String[] args) {
        List<String> array = Lists.newArrayList(
                "1-2-3-4", "-1-2--4-5-0-78-89", "2-3", "1-3-4");
        Node root = new Node("");
        root.setChildrenTokens(array);
        genTree(root);
        System.out.println(new Gson().toJson(root));
        int i = 1;
    }

    private static void genTree(Node node) {
        if (node.getChildrenTokens().isEmpty())
            return;
        for (String s : node.childrenTokens) {
            String key = extractKey(s);
            if (key != null) {
                String rest = extractRest(s, key);
                Node newNd = new Node(key);
                List<Node> children = node.getChildren();
                if (!children.contains(newNd))
                    children.add(newNd);
                if (!rest.isEmpty())
                    children.get(children.indexOf(newNd)).getChildrenTokens().add(rest);
            }
        }
        for (Node child : node.children) {
            genTree(child);
        }
    }

    private static String extractRest(String s, String key) {
        String rest = s.replaceFirst(key, "");
        if (!rest.isEmpty()) {
            rest = rest.substring(1);
        }
        return rest;
    }

    private static String extractKey(String s) {
        Pattern p = Pattern.compile("(-?\\d*)(--?\\d*)*");
        Matcher m = p.matcher(s);
        if (m.find())
            return m.group(1);
        return null;
    }

}