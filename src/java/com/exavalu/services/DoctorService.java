/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.exavalu.services;

import com.exavalu.models.Appointment;
import com.exavalu.models.Departments;
import com.exavalu.models.Doctors;
import com.exavalu.utils.JDBCConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author anich
 */
public class DoctorService {

    public static DoctorService doctorService = null;
    public static Logger log = Logger.getLogger(DoctorService.class.getName());

    private DoctorService() {
    }

    public static DoctorService getInstance() {
        if (doctorService == null) {
            return new DoctorService();
        } else {
            return doctorService;
        }
    }

    public ArrayList getAllDoctors(String departmentId) {
        ArrayList deptLIst = new ArrayList();
        try {

            Connection con = JDBCConnectionManager.getConnection();

            String sql = "Select * from doctors where departmentId=?";

            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setString(1, departmentId);

            System.out.println(preparedStatement);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Doctors doctors = new Doctors();

                doctors.setDoctorId(rs.getString("doctorId"));
                doctors.setDoctorFirstName(rs.getString("doctorFirstName"));
                doctors.setDoctorLastName(rs.getString("doctorLastName"));

                deptLIst.add(doctors);

            }

        } catch (SQLException ex) {
            int e = ex.getErrorCode();
            log.error(LocalDateTime.now() + "Sql Error :" + e + "Error in getting Doctors");
            System.out.println(LocalDateTime.now() + "Sql Error :" + e + "Error in getting Doctors");
        }

        return deptLIst;
    }

    public ArrayList doViewAppointments(String doctorId) {

        ArrayList appointmentList = new ArrayList();
        try {

            Connection con = JDBCConnectionManager.getConnection();

            String sql = "SELECT * FROM ohms_db.appointments right join doctors on doctors.doctorId=appointments.doctorId right join departments on departments.departmentId=appointments.departmentId right join patients on patients.patientId=appointments.patientId right join statusofappointments on statusofappointments.statusId=appointments.statusId where appointments.doctorId=?";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, doctorId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment();

                appointment.setAppointmentId(rs.getString("appointmentId"));
                appointment.setAppointmentDate(rs.getString("appointmentDate"));

                appointment.setPatientFirstName(rs.getString("patientFirstName"));
                appointment.setPatientLastName(rs.getString("patientLastName"));
                appointment.setGender(rs.getString("gender"));
                appointment.setAge(rs.getString("age"));
                appointment.setDepartmentName(rs.getString("departmentName"));
                //appointment.setSymptoms(rs.getString("symptoms"));

                appointmentList.add(appointment);

            }

        } catch (SQLException ex) {
            int e = ex.getErrorCode();
            log.error(LocalDateTime.now() + "Sql Error :" + e + "Error in getting Doctors");
            System.out.println(LocalDateTime.now() + "Sql Error :" + e + "Error in getting Doctors");
        }

        return appointmentList;
    }

    public String doViewBookings(String interval, String doctorId) {
        String totalBookings = null;

        try {
            Connection con = JDBCConnectionManager.getConnection();
            String sql = "SELECT COUNT(appointmentId) as bookings FROM appointments WHERE appointmentDate = DATE_ADD(CURDATE(), INTERVAL ? DAY) and doctorId=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, interval);
            ps.setString(2, doctorId);

            System.out.println("ps:" + ps);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalBookings = rs.getString("bookings");
            }

        } catch (SQLException ex) {
            Logger log = Logger.getLogger(AdminService.class.getName());
            log.error(LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.MEDIUM)) + " " + ex.getMessage());

        }
        return totalBookings;

    }

    public String doViewTotalRevenue(String interval, String doctorId) {

        String totalRevenue = null;

        try {
            Connection con = JDBCConnectionManager.getConnection();
            String sql = "SELECT SUM(amount) as totalRevenue FROM appointments where appointmentDate = DATE_ADD(CURDATE(), INTERVAL ? DAY) and doctorId=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, interval);
            ps.setString(2, doctorId);

            System.out.println("ps:" + ps);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalRevenue = rs.getString("totalRevenue");
            }

        } catch (SQLException ex) {
            Logger log = Logger.getLogger(AdminService.class.getName());
            log.error(LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.MEDIUM)) + " " + ex.getMessage());

        }
        return totalRevenue;
    }

    public ArrayList doViewtodayAppointments(String interval, String doctorId) {
        
        ArrayList appointmentList = new ArrayList();
        try {

            Connection con = JDBCConnectionManager.getConnection();

            String sql = "SELECT * FROM ohms_db.appointments right join doctors on doctors.doctorId=appointments.doctorId right join departments on departments.departmentId=appointments.departmentId right join patients on patients.patientId=appointments.patientId right join statusofappointments on statusofappointments.statusId=appointments.statusId where appointments.doctorId=? and  appointmentDate = DATE_ADD(CURDATE(), INTERVAL ? DAY)";

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, doctorId);
            preparedStatement.setString(2, interval);
            System.out.println(preparedStatement);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment();

                appointment.setAppointmentId(rs.getString("appointmentId"));
                appointment.setAppointmentDate(rs.getString("appointmentDate"));

                appointment.setPatientFirstName(rs.getString("patientFirstName"));
                appointment.setPatientLastName(rs.getString("patientLastName"));
                appointment.setGender(rs.getString("gender"));
                appointment.setAge(rs.getString("age"));
                appointment.setDepartmentName(rs.getString("departmentName"));
                appointment.setDoctorFirstName(rs.getString("doctorFirstName"));
                appointment.setDoctorLastName(rs.getString("doctorLastName"));
                appointment.setStatusId(rs.getString("statusId"));
                appointment.setStatus(rs.getString("statusName"));
             
                //appointment.setSymptoms(rs.getString("symptoms"));

                appointmentList.add(appointment);

            }

        } catch (SQLException ex) {
            int e = ex.getErrorCode();
            log.error(LocalDateTime.now() + "Sql Error :" + e + "Error in getting Doctors");
            System.out.println(LocalDateTime.now() + "Sql Error :" + e + "Error in getting Doctors");
        }

        return appointmentList;
    }

}
