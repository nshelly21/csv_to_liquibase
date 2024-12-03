import java.io.*;

public class Main {

    static String header = "<databaseChangeLog xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "                   xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\"\n" +
            "                   xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.6.xsd\">";
    static String footer = "</databaseChangeLog>";


    public static void main(String[] args) throws IOException {
        File myObj = new File("values.txt");
        FileInputStream fileInputStream = new FileInputStream(myObj);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

        String code = "";
        String content = "";
        String system = "";
        String subject = "";
        char[] buff = new char[568830];
        int csvInt = reader.read(buff);
        System.out.println(csvInt);
        StringBuilder sb1 = new StringBuilder(new String(buff));
        String[] rows = sb1.toString().split("\";<NR>");
        System.out.println(rows.length);
        StringBuilder sb = new StringBuilder();
        sb.append(header);
        sb.append("\n").append("\n");
        for (int i=1; i< rows.length - 1; i++) {
            String[] vals = rows[i].trim().replace("\"\"", "\"").replace("'", "''").split(";<BC>;\"");
            String colums = vals[0];
            content = vals[1];
            String[] values = colums.trim().split(";");
            if (i == 211)
                System.out.println(i);
            code = values[1];
            system = values[2];
            subject = values[3];
            sb.append(getModel(code, subject, system, content));
            sb.append("\n").append("\n");
        }
        sb.append(footer).append("\n");;
        String outputFileName = "fileOutput.xml";

        try (BufferedWriter writter = new BufferedWriter(new FileWriter(outputFileName))) {
            writter.write(sb.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getModelOld(String code, String subject, String system, String content) {
        return "    <changeSet id=\"update_notification_" + code.toLowerCase() + "\" author=\"d.tituana\">\n" +
                "        <comment>TEAMRSO-20354</comment>\n" +
                "        <update tableName=\"notification_template\">\n" +
                "            <column name=\"system\" value=\"" + system + "\"/>\n" +
                "            <column name=\"subject\" value=\"" + subject + "\"/>\n" +
                "            <column name=\"content\" value='" + content + "'/>\n" +
                "            <where>code = '" + code + "'</where>\n" +
                "        </update>\n" +
                "    </changeSet>";
    }

    public static String getModel(String code, String subject, String system, String content) {
        return "\t<changeSet id=\"update_notification_" + code.toLowerCase() + "\" author=\"d.tituana\">\n" +
                "\t\t<validCheckSum>ANY</validCheckSum>\n" +
                "\t\t<comment>TEAMRSO-20354</comment>\n" +
                "\t\t<sql>\n" +
                "\t\t\t<![CDATA[\n" +
                "\t\t\tUPDATE notification_template\n" +
                "\t\t\tSET system = " + system + ",\n" +
                "\t\t\t\tsubject = '" + subject + "',\n" +
                "\t\t\t\tcontent =\n" +
                "'" + content + "'\n" +
                "\t\t\tWHERE code = '" + code + "';\n" +
                "\t\t\t]]>\n" +
                "\t\t</sql>\n" +
                "\t</changeSet>";
    }

}
