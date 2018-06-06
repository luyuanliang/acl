package org.web.acl.domain.menu;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luyl on 17-11-20.
 */
@Setter
@Getter
public class MenuNode {

    //     "id": 1,
//             "text": "ROOT",
//             "children":
    private long id;
    private String text;
    private boolean checked = false;
    private transient String[] path;
    private Map<String, String> attributes;
    private List<MenuNode> children;

    public MenuNode(long id) {
        this.id = id;
        children = Lists.newArrayList();
    }

    public Map<String, String> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes;
    }

    public MenuNode getIdxChild(String step) {
        for (MenuNode child : children) {
            String[] childPath = child.getPath();
            if (childPath[childPath.length - 1].endsWith(step))
                return child;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MenuNode && ((MenuNode) obj).id == this.id;
    }

}
