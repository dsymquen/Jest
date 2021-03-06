package io.searchbox.core;

import io.searchbox.AbstractAction;

import java.util.HashSet;
import java.util.List;

/**
 * @author Dogukan Sonmez
 */


public class MultiGet extends AbstractAction {

    protected MultiGet() {
    }

    public MultiGet(List<Doc> docs) {
        setURI("_mget");
        setBulkOperation(true);
        setData(prepareMultiGet(docs));
        setPathToResult("docs/_source");
    }

    public MultiGet(String index, String type, String[] ids) {
        setBulkOperation(true);
        setURI("/" + index + "/" + type + "/_mget");
        setData(prepareMultiGet(ids));
        setPathToResult("docs/_source");
    }

    public MultiGet(String index, String[] ids) {
        setURI("/" + index + "/_mget");
        setData(prepareMultiGet(ids));
        setBulkOperation(true);
        setPathToResult("docs/_source");
    }

    protected Object prepareMultiGet(List<Doc> docs) {
        //[{"_index":"twitter","_type":"tweet","_id":"1","fields":["field1","field2"]}
        StringBuilder sb = new StringBuilder("{\"docs\":[");
        for (Doc doc : docs) {
            sb.append("{\"_index\":\"")
                    .append(doc.getIndex())
                    .append("\",\"_type\":\"")
                    .append(doc.getType())
                    .append("\",\"_id\":\"")
                    .append(doc.getId())
                    .append("\"");
            if (doc.getFields().size() > 0) {
                sb.append(",");
                sb.append(getFieldsString(doc.getFields()));
            }
            sb.append("}");
            sb.append(",");
        }
        sb.delete(sb.toString().length() - 1, sb.toString().length());
        sb.append("]}");
        return sb.toString();
    }

    private Object getFieldsString(HashSet<String> fields) {
        //"fields":["field1","field2"]
        StringBuilder sb = new StringBuilder("\"fields\":[");
        for (String val : fields) {
            sb.append("\"")
                    .append(val)
                    .append("\"")
                    .append(",");
        }
        sb.delete(sb.toString().length() - 1, sb.toString().length());
        sb.append("]");
        return sb.toString();
    }

    protected Object prepareMultiGet(String[] ids) {
        //{"docs":[{"_id":"1"},{"_id" : "2"},{"_id" : "3"}]}
        StringBuilder sb = new StringBuilder("{\"docs\":[")
                .append(concatenateArray(ids))
                .append("]}");
        return sb.toString();
    }

    private String concatenateArray(String[] values) {
        StringBuilder sb = new StringBuilder();
        for (String val : values) {
            sb.append("{\"_id\":\"")
                    .append(val)
                    .append("\"}")
                    .append(",");
        }
        sb.delete(sb.toString().length() - 1, sb.toString().length());
        return sb.toString();
    }

    @Override
    public String getPathToResult() {
        return "docs/_source";
    }

    @Override
    public String getRestMethodName() {
        return "GET";
    }
}
