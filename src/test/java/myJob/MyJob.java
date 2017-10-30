package myJob;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jobInfo.JobInfo;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Unit test for simple App.
 */
public class MyJob implements PageProcessor {

	public int pageCount;// 该职位的总页数
	private int first = 0;// 标志位,代表总页数只获取一次

	private String jobName;// 职位名称
	private String salary;// 职位薪水
	private String education;// 学历要求
	private String[] experience;// 工作经验
	private String jobTemptation;// 职位诱惑
	private String companyName;// 公司名称
	private String scale;// 公司规模
	private String financing;// 融资情况
	private String field;// 公司所处领域
	private String[] city;// 工作城市
	private String url;// 公司网址
	private String address, address1, address2;// 工作地址
	public final String job_lagou = "https://www\\.lagou\\.com/zhaopin/ceshijingli2/\\w/\\?filterOption=\\w";
	public final String job_detail = "https://www\\.lagou\\.com/jobs/\\w+\\.html";

	JobSave jobSave = new JobSave();

	public void process(Page page) {
		// TODO Auto-generated method stub
		JobInfo jobInfo = new JobInfo();
		// 获取要查询的职位一共有多少页
		if (first == 0) {

			pageCount = Integer.parseInt(page.getHtml()
					.xpath("//*[@id='order']/li/div[4]/div/span[2]/text()")
					.toString());
			System.out.println("一共有:" + pageCount + "页");
			jobSave.sqlDelete();
			first = 1;
		}

		List<String> jobList = new ArrayList<String>();
		// 添加所有的列表页到要爬取的队列中
		for (int i = 1; i <= pageCount; i++) {

			jobList.add("https://www.lagou.com/zhaopin/ceshijingli2/" + i
					+ "/?filterOption=" + i);
			page.addTargetRequests(jobList);
		}

		// 用正则判断是否是列表页
		if (page.getUrl().regex(job_lagou).match()) {
			// 获取所有详情页的链接添加到目标URL
			List<String> urls = page.getHtml()
					.xpath("//*[@id='s_position_list']/ul").links()
					.regex(job_detail).all();

			for (String url : urls) {

				System.out.println("详情页的链接地址" + url);
				page.addTargetRequest(url);

			}
		} else if (page.getUrl().regex(job_detail).match()) {

			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 职位名称
			jobName = page.getHtml()
					.xpath("/html/body/div[2]/div/div[1]/div/span/text()")
					.toString().trim();
			// 职位薪水
			salary = page
					.getHtml()
					.xpath("/html/body/div[2]/div/div[1]/dd/p[1]/span[1]/text()")
					.toString().trim();
			// 学历要求
			education = page.getHtml()
					.xpath("/html/body/div[2]/div/div[1]/dd/p[1]/span[4]")
					.toString().substring(6, 11).trim();
			// 职位所在城市
			city = page.getHtml()
					.xpath("//*dd[@class=job_request]/p[1]/span[2]/text()")
					.toString().trim().split("/");
			// 职位所需经验
			experience = page
					.getHtml()
					.xpath("/html/body/div[2]/div/div[1]/dd/p[1]/span[3]/text()")
					.toString().trim().split("/");
			// 公司名称
			companyName = page.getHtml()
					.xpath("//*[@id='job_company']/dt/a/div/h2/text()")
					.toString().trim();
			// 公司规模
			scale = page.getHtml()
					.xpath("//*[@id='job_company']/dd/ul/li[3]/text()")
					.toString().trim();

			if (scale.equals("   ")) {
				scale = page.getHtml()
						.xpath("//*[@id='job_company']/dd/ul/li[4]/text()")
						.toString().trim();
			}
			// 职位诱惑
			jobTemptation = page.getHtml()
					.xpath("//*[@id='job_detail']/dd[1]/p/text()").toString()
					.trim();
			// 公司融资情况
			financing = page.getHtml()
					.xpath("//*[@id='job_company']/dd/ul/li[2]/text()")
					.toString().trim();
			// 公司网址

			url = page.getHtml()
					.xpath("//*[@id='job_company']/dd/ul/li[4]/a/text()")
					.toString();
			if ("".equals(url) || url == null) {

				url = page.getHtml()
						.xpath("//*[@id='job_company']/dd/ul/li[5]/a/text()")
						.toString().trim();
				System.out.println("确实走这里了,并且有值" + url);
			}
			// 公司所处领域
			field = page.getHtml()
					.xpath("//*[@id='job_company']/dd/ul/li[1]/text()")
					.toString().trim();
			// 下面是公司具体地址
			address = page.getHtml()
					.xpath("//*[@id='job_detail']/dd[3]/div[1]/a[1]/text()")
					.toString().trim();
			address1 = page.getHtml()
					.xpath("//*[@id='job_detail']/dd[3]/div[1]/a[2]/text()")
					.toString().trim();
			address2 = page.getHtml()
					.xpath("//*[@id='job_detail']/dd[3]/div[1]/text()")
					.toString();

			// 判断"-"在地址中出现的次数，以此判断截取的长度
			int count = jobSave.appearNumber(address2, "-");
			if (count == 2) {

				address = address + "-" + address1 + address2.substring(4);
			} else if (count == 3) {

				address = address + "-" + address1 + address2.substring(8);
			} else {

				address = address + "-" + address1 + address2;
			}


			jobInfo.setJobName(jobName);
			jobInfo.setSalary(salary);
			jobInfo.setEducation(education);
			jobInfo.setExperience(experience[0]);
			jobInfo.setJobTemptation(jobTemptation);
			jobInfo.setCompanyName(companyName);
			jobInfo.setField(field);
			jobInfo.setScale(scale);
			jobInfo.setFinancing(financing);
			jobInfo.setCity(city[1]);
			jobInfo.setAddress(address);
			jobInfo.setUrl(url);

			jobSave.jobSave(jobInfo);
			System.out.println("职位名称:" + jobName.trim());
			System.out.println("薪水:" + salary.trim());
			System.out.println("学历要求:" + education);
			System.out.println("工作经验:" + experience[0]);
			System.out.println("职位诱惑:" + jobTemptation.trim());
			System.out.println("公司名称:" + companyName.trim());
			System.out.println("公司领域:" + field.trim());
			System.out.println("公司规模:" + scale.trim());
			System.out.println("公司融资:" + financing.trim());
			System.out.println("工作城市:" + city[1]);
			System.out.println("公司地址:" + address.trim());
			System.out.println("公司url" + url.trim());
		} else {

			System.out.println("拉钩爬虫");
		}

	}

	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	String ua_list[] = {
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1",
			"Mozilla/5.0 (X11; CrOS i686 2268.111.0) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6",
			"Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1090.0 Safari/536.6", };
	Random rand = new Random();
	int num = rand.nextInt(4);
	private Site site = Site
			.me()
			.setDomain("www.lagou.com")
			.setSleepTime(3000)
			.setRetryTimes(3)
			.setUserAgent(ua_list[num])
			.addCookie("user_trace_token",
					"20170511104430-5a5aae797386438c8d067e5d2b4a50bc")
			.addCookie("LGUID",
					"20171025183941-ce812d05-b970-11e7-a827-525400f775ce")
			.addCookie("JSESSIONID",
					"ABAAABAACDBAAIAEE98535AAA50BB6C856192B158962BF2")
			.addCookie("X_HTTP_TOKEN", "96d96098db41e0689ef2589328980144");

	public static void main(String[] args) {

		Spider.create(new MyJob())
				.addUrl("https://www.lagou.com/zhaopin/ceshijingli2/1/?filterOption=1")
				.thread(6)// 启用2个线程
				.run();

	}

}
