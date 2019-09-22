package team.ag.knmap.service.impl;

import com.franz.agraph.repository.AGRepositoryConnection;
import com.franz.agraph.repository.AGTupleQuery;
import com.franz.agraph.repository.AGValueFactory;
import com.google.gson.Gson;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.ag.knmap.commom.AGDBConnection;
import team.ag.knmap.commom.ServerResponse;
import team.ag.knmap.service.AllegrographService;
import team.ag.knmap.vo.TriadVo;

import java.util.*;

/**
 * @Author weitangzhao
 **/
@Service
public class AllegrographServiceImpl implements AllegrographService {

    @Autowired
    AGDBConnection agdbConnection;

    @Autowired
    TemplateClassServiceImpl templateClassService;

    @Override
    public void AddTriad(TriadVo triadVo) {

    }

    /**
     *
     * @param triple 用来获取值的URI
     * @return String类型的URI的值
     */
    public static String getValueFromIRI(Value triple)
    {
        String[] tripleList = triple.toString().split("/");
        return tripleList[tripleList.length - 1];
    }

    @Override
    public ServerResponse searchGraphInfoByKey(String repositoryID, String key) {
        Map<String, String> data = new HashMap<String, String>();		// 返回值，两个键分别为节点及边
        Gson gson = new Gson();										// 将返回值中的列表值转换为json

        // 节点的主键
        Integer rootName = 0;
        Integer branchName = 0;
        Integer leafName = 0;
        // 获取链接对象，并根据key找到节点
        AGRepositoryConnection conn = agdbConnection.create(repositoryID);
        AGValueFactory vf = conn.getValueFactory();
        // String queryString = "SELECT ?s ?p ?o WHERE {?s ?p ?o .}";
        // conn.createFreetextIndex("index1", new IRI[]{ vf.createIRI("http://example.org/ontology/参股控股公司") });
        String queryString = "SELECT ?s ?p ?o WHERE {?s ?p ?o .}";
        AGTupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        tupleQuery.setBinding("s", vf.createIRI("http://example.org/place/"+key));
        TupleQueryResult result = tupleQuery.evaluate();
        List<Map<String, Comparable>> nodeList = new ArrayList<Map<String, Comparable>>();		// 用来保存节点的列表
        List<Map<String, String>> linkList = new ArrayList<Map<String, String>>();  			// 用来保存边的列表

        Set<String> nodeSet = new HashSet<>();
        nodeSet.add(key);
        // 获取分支节点
        while(result.hasNext()) {
            BindingSet bindingSet = result.next();	// 获取分支结点的信息，以便获取叶节点的信息
            Map<String, Comparable> branchNode = new HashMap<String, Comparable>();
            // 分支节点
            Value branchValue = bindingSet.getValue("o");
            if(!(getValueFromIRI(bindingSet.getValue("o")).equals(key))){
                //branchNode.put("name", getValueFromIRI(branchValue));
                //nodeList.add(branchNode);
                nodeSet.add(getValueFromIRI(branchValue));
            }
            // 指向根节点的边
            Map<String, String> toRootLink = new HashMap<String, String>();
            toRootLink.put("source", getValueFromIRI(branchValue));
            toRootLink.put("target", key);
            toRootLink.put("name", getValueFromIRI(bindingSet.getValue("p")));
            linkList.add(toRootLink);			   //将指向根节点加入边列表
        }
        Iterator<String> it = nodeSet.iterator();
        HashSet<String> hashSet = new HashSet<>();
        while(it.hasNext()) {
            String nodeValue = it.next();
            nodeValue = nodeValue.trim();
            if(!hashSet.contains(nodeValue)) {
                Map<String, Comparable> NodeMap = new HashMap<String, Comparable>();
                NodeMap.put("name", nodeValue);
                nodeList.add(NodeMap);
                hashSet.add(nodeValue);
            }
        }
        String nodeListJson = gson.toJson(nodeList);
        String linkListJson = gson.toJson(linkList);
        data.put("nodeList", nodeListJson);
        data.put("linkList", linkListJson);
        return ServerResponse.createBySuccess(data);
    }

}
