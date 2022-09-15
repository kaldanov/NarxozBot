package com.example.spgtu.service;

import com.example.spgtu.dao.entities.custom.Vedomost;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBC {
    public static Statement st;

    public static boolean initIt() {
        try {

//            DriverManager.registerDriver(new SQLServerDriver());
            String url = "jdbc:jtds:sqlserver://192.168.225.14:1433/workdatabase";

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, "bot", "b7dV6D");
            st = conn.createStatement();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Vedomost> getAllVedomost(String login){
        List<Integer> vedsIds = getVedsIds(login);

        List<Vedomost> vedomosts = new ArrayList<>();
        for (Integer vedId : vedsIds) {
            try {
                String sql = "select * from dbo.lp_Vedomost where Archive = 0 and Ved_ID =" + vedId;
                ResultSet resultSet = st.executeQuery(sql);
                while (resultSet.next()) {
                    Vedomost vedomost = new Vedomost();
                    vedomost.setId(resultSet.getInt("Ved_ID"));
                    vedomost.setLearnYear(resultSet.getInt("LearnYear_ID"));
                    vedomost.setVedNo(resultSet.getInt("Ved_No"));
                    vedomost.setFacCode(resultSet.getInt("FacCode"));
                    vedomost.setGroupNo(resultSet.getInt("Group_No"));
                    vedomost.setSemester(resultSet.getInt("Semestr"));
                    vedomost.setLpDis_id(resultSet.getInt("lpDis_ID"));
                    vedomost.setVedType(resultSet.getInt("VedType"));
//                    vedomost.setArchive(resultSet.getInt("Archive"));
                    vedomost.setSubGroupNo(resultSet.getInt("SubGroup_No"));
                    vedomost.setStatus(resultSet.getInt("Status"));
                    vedomost.setDis_Name(resultSet.getString("Dis_Name"));
                    vedomosts.add(vedomost);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return vedomosts;

    }

    private static List<Integer> getVedsIds(String login){
        Integer studId = getStudentIdByLogin(login);
        String sql= "select Ved_ID from dbo.lp_VedStud where Mark = 0 and StudNo =" + studId;
        List<Integer> vedsId = new ArrayList<>();
        try {
            ResultSet resultSet = st.executeQuery(sql);
            while (resultSet.next()){
                vedsId.add(resultSet.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vedsId;

    }

    private static Integer getStudentIdByLogin(String login) {
        final char dm = (char) 39;
        String sql = "select StudEmpNo from dbo.Net_Users where Login =" + dm + login + dm;
        try {
            ResultSet resultSet = st.executeQuery(sql);
            if (resultSet.next())
               return resultSet.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}