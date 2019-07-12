package team.ag.knmap.service;

import com.franz.agraph.repository.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.rdf4j.model.*;

import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author weitangzhao
 **/

@Component
public class TutorialExamples {

    private static final Logger LOG =  LogManager.getLogger(TutorialExamples.class);
    private static final String SERVER_URL ="http://47.103.11.118:10035";
    //"scratch"
    private static final String CATALOG_ID = "system";
    private static final String REPOSITORY_ID = "test";
    private static final String USERNAME = getenv("AGRAPH_USER", "test");
    private static final String PASSWORD = getenv("AGRAPH_PASS", "xyzzy");
    private static final File DATA_DIRECTORY = new File(getenv("AGRAPH_DATA", "D:\\ProJect_Practice\\knmap\\src\\main\\resources\\data\\"));
    private static final String TEMPORARY_DIRECTORY = getenv("AGRAPH_TEMP", "");

    private static final String FOAF_NS ="http://xmlns.com/foaf/0.1/";

    private static List<AutoCloseable> toClose = new ArrayList<AutoCloseable>();


    /**
     * Gets the value of an environment variable.
     * @param name Name of the variable.
     * @param defaultValue Value to be returned if the varaible is not defined.
     * @return Value.
     */
    private static String getenv(final String name, final String defaultValue) {
        final String value = System.getenv(name);
        return value != null ? value : defaultValue;
    }


    /***
     *  创建存储库
     */
    public static AGRepositoryConnection example1(boolean close)throws Exception {
        LOG.info("\nStaeting examplel()。");
        AGServer server = new AGServer(SERVER_URL,USERNAME,PASSWORD);
        LOG.info("Available catalogs：" + server.listCatalogs());

        /**
         * 调查AllegroGraph服务器上的目录
         */
        AGCatalog catalog = server.getCatalog();
        LOG.info("目录中的可用存储库"+ catalog.getCatalogName() +":"+catalog.listRepositories());

        /**
         * 储库对象的getConnection（）方法返回此连接对象
         * 函数closeBeforeExit（）维护一个连接对象列表，并在客户端退出时自动清除它们。
         */
        AGRepository myRepository = catalog.openRepository(REPOSITORY_ID);
        AGRepositoryConnection conn = myRepository.getConnection();
        closeBeforeExit(conn);

        /**
         * 在默认模式下，example1（）关闭连接。
         * 当被另一个方法调用时，它可以选择性地返回连接。
         * 如果您已完成连接，关闭它并关闭它将释放资源。
         */
        if (close) {
            conn.close();
            myRepository.shutDown();
            return null;
        }
        return conn;
    }

    public void example101() throws Exception{
        RepositoryConnection conn = example1(false);
        try{

            IRI alice = conn.getValueFactory().createIRI("http://www.gemantic.com/knowledge/fo_2.owl#珠海市智明有限公司");
            RepositoryResult<Statement> statements = conn.getStatements(alice, null, null, false);
            try {
                //有时需要使用enableDuplicateFilter（）方法筛选重复项的结果。
                //但请注意，重复过滤可能很昂贵。我们的示例不包含任何重复项，但它们可能会发生。
                //statements.enableDuplicateFilter();
                while (statements.hasNext()) {
                    LOG.info(statements.next());
                }
            } finally {
                statements.close();
            }

        }finally {
            conn.close();
        }
    }

    public void example100() throws Exception {
        String key = "唐人神集团股份有限公司";
        RepositoryConnection conn = example1(false);

        try {

            //String queryString = "SELECT ?s ?p ?o  WHERE {?s ?p ?o .}limit 100";
/**
            String queryString ="SELECT ?s ?p ?o\n" +
                    "WHERE\n" +
                    "{\n" +
                    "?s ?p ?o .\n" +
                    "FILTER (regex(?o, \"雅虎\",\"i\"))\n" +
                    "}\n" +
                    "limit 100"**/

            String queryString="PREFIX foaf: <http://www.gemantic.com/knowledge/fo_2.owl#>\n" +
                    "\n" +
                    "SELECT ?p ?o\n" +
                    "WHERE\n" +
                    "{\n" +
                    "  foaf:湖南湘大兽药有限公司 ?p ?o .\n" +
                    " \n" +
                    "}\n" +
                    "limit 100\n";
            AGTupleQuery tupleQuery = (AGTupleQuery) conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();
            while (result.hasNext()) {
                BindingSet bindingSet = result.next();
                Value s = bindingSet.getValue("s");
                Value p = bindingSet.getValue("p");
                Value o = bindingSet.getValue("o");
                System.out.format("%s\n %s\n %s\n\n", s, p, o);
            }
        }finally {
            conn.close();
        }

    }
    /**
     * getStatements（）和SPARQL直接搜索返回您要求的数据类型。
     * SPARQL过滤器查询有时可以返回多种数据类型。此行为将是本节的重点之一。
     * 如果您没有明确表示值的数据类型，则在声明三元组或编写搜索模式时，AllegroGraph将推导出适当的数据类型并使用它。
     * 这是本节的另一个重点。这种有用的行为有时会让您感到意想不到的结果。
     * @throws Exception
     */
    public static void example5() throws Exception {

        /**
         * 在本节中，我们将设置各种字符串三元组，并尝试使用getStatements（）和SPARQL进行匹配。
         * 我们将对整个字符串进行简单匹配。
         *
         */
        // Example5（）首先从example1（）获取连接对象，然后清除所有现有三元组的存储库。
        RepositoryConnection conn = example1(false);
        Repository myRepository = conn.getRepository();
        ValueFactory f = myRepository.getValueFactory();
        LOG.info("\nStarting example5().");
        conn.clear();

        //为命名空间字符串创建变量
        String exns = "http://people/";

        //创建了描述七个人的新资源，按照字母顺序从Alice到Greg命名。
        IRI alice = f.createIRI("http://people/alice");
        IRI bob = f.createIRI("http://people/bob");
        IRI carol = f.createIRI("http://people/carol");
        IRI dave = f.createIRI("http://people/dave");
        IRI eric = f.createIRI(exns, "eric");
        IRI fred = f.createIRI(exns, "fred");
        IRI greg = f.createIRI(exns,"greg");
        /**
         * 首先，我们需要一个谓词
         */
        IRI favoriteColor = f.createIRI(exns, "favoriteColor");

        /**
         * 创建各种字符串值，以及单个“普通文字”值。
         */
        Literal UCred = f.createLiteral("Red");
        Literal LCred = f.createLiteral("red");
        Literal RedPlain = f.createLiteral("Red");
        Literal rouge = f.createLiteral("rouge", XMLSchema.STRING);
        Literal Rouge = f.createLiteral("Rouge", XMLSchema.STRING);
        Literal RougePlain = f.createLiteral("Rouge");
        Literal FrRouge = f.createLiteral("Rouge", "fr");

        //构建Statements语句也可以 或者采用下面的方式添加新三元组
        conn.add(alice, favoriteColor, UCred);
        conn.add(bob, favoriteColor, LCred);
        conn.add(carol, favoriteColor, RedPlain);
        conn.add(dave, favoriteColor, rouge);
        conn.add(eric, favoriteColor, Rouge);
        conn.add(fred, favoriteColor, RougePlain);
        conn.add(greg, favoriteColor, FrRouge);

        //此循环将所有年龄三元组打印到交互窗口。请注意，检索到的三元组有六种类型
        LOG.info("\nShowing all age triples using getStatements().  Seven matches.");
        RepositoryResult<Statement> statements = conn.getStatements(null, favoriteColor, null, false);
        try {
            while (statements.hasNext()) {
                LOG.info(statements.next());
            }
        } finally {
            statements.close();
        }
    }



    public static AGRepositoryConnection example6() throws Exception {
        /**
        AGServer server = new AGServer(SERVER_URL, USERNAME, PASSWORD);
        AGCatalog catalog = server.getCatalog(CATALOG_ID);
        AGRepository myRepository = catalog.createRepository(REPOSITORY_ID);
        myRepository.initialize();
        AGRepositoryConnection conn = myRepository.getConnection();
         **/
        /**
        AGRepositoryConnection conn = AGServer.createRepositoryConnection(
                REPOSITORY_ID, CATALOG_ID, SERVER_URL, USERNAME, PASSWORD);
         **/

        AGServer server = new AGServer(SERVER_URL, USERNAME, PASSWORD);
        AGCatalog catalog = server.getCatalog();
        if (catalog == null) {
            throw new Exception("Catalog " + CATALOG_ID + " does not exist. Either "
                    + "define this catalog in your agraph.cfg or modify the CATALOG_ID "
                    + "in this tutorial to name an existing catalog.");
        }

        LOG.info(catalog.listRepositories());
        AGRepository myRepository = catalog.createRepository(REPOSITORY_ID);
        myRepository.initialize();
        LOG.info("Repository is writable? " + myRepository.isWritable());
        LOG.info("目录中的可用存储库"+ catalog.getCatalogName() +":"+catalog.listRepositories());
        AGRepositoryConnection conn = myRepository.getConnection();
        LOG.info("Repository " + (myRepository.getRepositoryID()) +
                " is up! It contains " + (conn.size()) +
                " statements."
        );
        closeBeforeExit(conn);
        conn.clear();

        //conn.begin();  // start a transaction 替换conn.setAutoCommit(false);
        ValueFactory f = conn.getValueFactory();

        File path1 = new File(DATA_DIRECTORY, "java-vcards.rdf");
        File path2 = new File(DATA_DIRECTORY , "java-kennedy.ntriples");

        String baseURI = "http://example.org/example/local";

        IRI context = f.createIRI("http://example.org#vcards");

        // read vcards triples into the context 'context':
        conn.add(path1, baseURI, RDFFormat.RDFXML, context);
        // read Kennedy triples into the null context:
        conn.add(path2, baseURI, RDFFormat.NTRIPLES);

       LOG.info("After loading, repository contains " + conn.size(context) +
                " vcard triples in context '" + context + "'\n    and   " +
                conn.size((Resource)null) + " kennedy triples in context 'null'.");

       conn.commit();

        LOG.info("\nMatch all and print subjects and contexts");
        RepositoryResult<Statement> result = conn.getStatements(null, null, null, false);
        for (int i = 0; i < 25 && result.hasNext(); i++) {
            Statement stmt = result.next();
            LOG.info(stmt.getSubject() + "  " + stmt.getContext());
        }
        result.close();
        return conn;
    }

    public static void example7() throws Exception {
        RepositoryConnection conn = example1(false);
        LOG.info("\nMatch all and print subjects and contexts");
        RepositoryResult<Statement> result = conn.getStatements(null, null, null, false);
        for (int i = 0; i < 25 && result.hasNext(); i++) {
            Statement stmt = result.next();
            LOG.info(stmt.getSubject() + "  " + stmt.getContext());
        }
        result.close();

    }
    static void close(AutoCloseable conn) {
        try {
            conn.close();
        } catch (Exception e) {
            System.err.println("Error closing repository connection: " + e);
            e.printStackTrace();
        }
    }



    /**
     * This is just a quick mechanism to make sure all connections get closed.
     */
    protected static void closeBeforeExit(AutoCloseable conn) {
        toClose.add(conn);
    }

    protected static void closeAll() {
        while (!toClose.isEmpty()) {
            AutoCloseable conn = toClose.get(0);
            close(conn);
            while (toClose.remove(conn)) {
                // ...
            }
        }
    }


}
