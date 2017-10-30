package myJob;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jobInfo.JobInfo;
import jobUtil.DBUtils;

public class JobSave {

	static DBUtils dbUtils = new DBUtils();
	StringBuffer sql = new StringBuffer();
	private int status;

	public int jobSave(JobInfo jobInfo) {

		String sql = "insert into myJob(JobName, Salary, Education, Experience, JobTemptation, CompanyName, Filed, "
				+ "Scale, Financing, City, Address, Website) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		
		List<String> sqlValues = new ArrayList<>();
		sqlValues.add(jobInfo.getJobName());
		sqlValues.add(jobInfo.getSalary());
		sqlValues.add(jobInfo.getEducation());
		sqlValues.add(jobInfo.getExperience());
		sqlValues.add(jobInfo.getJobTemptation());
		sqlValues.add(jobInfo.getCompanyName());
		sqlValues.add(jobInfo.getField());
		sqlValues.add(jobInfo.getScale());
		sqlValues.add(jobInfo.getFinancing());
		sqlValues.add(jobInfo.getCity());
		sqlValues.add(jobInfo.getAddress());
		sqlValues.add(jobInfo.getUrl());
		
		int result = dbUtils.executeUpdate(sql.toString(), sqlValues);
		return result;
	}
	
	public int sqlDelete(){
		
		String sqlDelete="TRUNCATE TABLE myJob";
		
		try {
			
			status=dbUtils.delete(sqlDelete);
			System.out.println("删除是否成功:"+ status);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
		
		
	}
	
	/**
	 * public int indexOf(int ch, int fromIndex)
	 * 返回在此字符串中第一次出现指定字符处的索引，从指定的索引开始搜索
	 * 
	 * @param srcText
	 * @param findText
	 * @return
	 */
	public int appearNumber(String srcText, String findText) {
	    int count = 0;
	    int index = 0;
	    while ((index = srcText.indexOf(findText, index)) != -1) {
	        index = index + findText.length();
	        count++;
	    }
	    return count;
	}
}
