import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.csvreader.CsvWriter;


public class LazadaScraper {
     public WebDriver driver;
     public WebDriver driver2;
     public WebDriver magneto;
     public String url="http://www.lazada.co.th/shop-food-supplements-weight-management/?page=";
     public String writeFileName="output.csv";
     
     int p=1;
     
     public void getProduct(String url){
    	 driver2.get(url);
    	 System.out.println("Reading Product ");
    	 Map<String,String> data=new HashMap<String, String>();
    	 data.put("url_scrap", url);
    	 try{
    		 String productName=driver2.findElement(By.cssSelector("span.prd-title")).getText();
    		 data.put("productName", productName);
    		 System.out.println("Product Name: "+productName);
    	 }catch(Exception e){}
    	 try{
    		 String productPrice=driver2.findElement(By.id("special_price_box")).getText();
    		 data.put("productPrice", productPrice);
    		 System.out.println("Product Price: "+productPrice);
    	 }catch(Exception e){}
    	 
    	 try{
    		 String productSize=driver2.findElement(By.xpath("//div[2]/div[3]/ul/li[3]")).getText();
    		 data.put("productSize", productSize);
    		 System.out.println("Product Size: "+productSize);
    	 }catch(Exception e){}
    	 
    	 try{	
    		 String productDescription=driver2.findElement(By.cssSelector("div.prd-description")).getText();
    		 data.put("productDesc", productDescription);
    		 System.out.println("Product Desc: "+productDescription);
    	 }catch(Exception e){}
    	 try{
    		  	 
		    	 String productImage=driver2.findElement(By.xpath("/html/body/div/div/article/div/div[2]/div/section/div/div/div[3]/div/div/div/ul/li")).getAttribute("data-image-product");
		    	 
		    	 for(int i=1;;i++){
				    	 try{
				    		 productImage=productImage+",  "+driver2.findElement(By.xpath("/html/body/div/div/article/div/div[2]/div/section/div/div/div[3]/div/div/div/ul/li["+i+"]")).getAttribute("data-image-product");
				    	 }
				    	 catch(Exception e){
				    		 break;
				    	 }
		    	 }
		    	 data.put("productImage", productImage);
		    	 System.out.println("Product Image: "+productImage);
    	 }catch(Exception e){}

    	upload(data);
    	 //writeCSV(data);
    	
    	
    	 
    	  
     }
     
     
     public void uploadImages(Map<String,String> data){
    	 String[] imageUrls=  data.get("productImage").split(",  ");
    	 System.out.println("Downloading Files");
    	 for(int i=0;i<imageUrls.length;i++){
	    	 try{
	 			URL website = new URL(imageUrls[i]);
	     	    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
	     	    String path=imageUrls[i].substring(imageUrls[i].lastIndexOf("/")+1);
	     	    FileOutputStream fos = new FileOutputStream(path);
	     	    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	     	    fos.close(); 
	 		}
	 		catch(Exception e){}
    	 }
    	 System.out.println("Download Compete");
    	 JOptionPane.showMessageDialog(null,
 			    "Please upload Images I have downloaded. Hit OK when done",
 			    "Instruction",
 			    JOptionPane.WARNING_MESSAGE); 
    	 for(int i=0;i<imageUrls.length;i++){
	    	 String path=imageUrls[i].substring(imageUrls[i].lastIndexOf("/")+1);
	    	 File file = new File(path);
	    	 if(file.delete()){
	    			System.out.println(file.getName() + " is deleted!");
	    		}else{
	    			System.out.println("Delete operation is failed.");
	    		}
    	 }
    	 
    	 
    	 
     }
     
     public void signIn(){
    	 magneto.get("https://peppercommerce.gostorego.com/admin");
    	 magneto.findElement(By.id("username")).sendKeys("guoweiz");
    	 magneto.findElement(By.id("login")).sendKeys("Stein123");
    	 magneto.findElement(By.xpath("//button[@type='submit']")).click();
    	 try{
	    	 new WebDriverWait(magneto, 60).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='nav']/li[3]/ul/li/a/span")));
	    	 magneto.findElement(By.xpath("//ul[@id='nav']/li[3]/ul/li/a/span")).click();
    	 }catch(Exception e){}
    	 
     }
     
     
     public void upload(Map<String,String> data){
    	 try{
			    	 try{
			    		 JOptionPane.showMessageDialog(null,
			  	 			    "Make Sure you are on product page",
			  	 			    "Instruction",
			  	 			    JOptionPane.WARNING_MESSAGE); 
			    	
			    		 magneto.findElement(By.xpath("//td[2]/button")).click();
			    	 }catch(Exception e){
			    		 JOptionPane.showMessageDialog(null,
			   	 			    "Some unknown error at add product. Please use your hand and click add product",
			   	 			    "Instruction",
			   	 			    JOptionPane.WARNING_MESSAGE); 
			    	 }
			    	 try{
				    	 new WebDriverWait(magneto, 60).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@id='continue_button']/button")));
				    	 magneto.findElement(By.xpath("//span[@id='continue_button']/button")).click();
			    	 }catch(Exception e){}
    	 try{
		    	 System.out.println("Writing Products Now");
		    	 new WebDriverWait(magneto, 60).until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
		    	 magneto.findElement(By.id("name")).sendKeys(data.get("productName"));
		    	 magneto.findElement(By.id("description")).sendKeys(data.get("productDesc"));
		    	 magneto.findElement(By.id("sku")).sendKeys("extLZ00"+p);
		    	 p++;
		    	 magneto.findElement(By.id("url_scrap")).sendKeys(data.get("url_scrap"));
		    	 magneto.findElement(By.id("size")).sendKeys(data.get("productSize"));
		    	 Select select = new Select(magneto.findElement(By.id("status")));
		    	 select.selectByIndex(1);
		    	 try{
		    		 JavascriptExecutor js = (JavascriptExecutor)magneto;
		    		 js.executeScript("javascript:window.scrollBy(250,350)");
		    		 magneto.findElement(By.id("productDesc"));
		    		 magneto.findElement(By.xpath("//div[@id='content']/div/div/div/p/button[4]")).click();
		    	 }catch(Exception e){
		    		 JOptionPane.showMessageDialog(null,
		 	 			    "Error while saving. Please use your hand",
		 	 			    "Instruction",
		 	 			    JOptionPane.WARNING_MESSAGE); 
		    	 }
    	 }catch(Exception e){}
    	 
    	 
    	 try{
	    	 magneto.findElement(By.xpath("//a[@id='product_info_tabs_group_5']/span")).click();
	    	 new WebDriverWait(magneto, 60).until(ExpectedConditions.presenceOfElementLocated(By.id("price")));
	    	 magneto.findElement(By.id("price")).sendKeys(data.get("productPrice"));
	    	 magneto.findElement(By.xpath("//div[@id='content']/div/div/div/p/button[4]")).click();
    	 }catch(Exception e){}
    	 
    	 try{
    		 Thread.sleep(9000);
    	 magneto.findElement(By.xpath("//a[@id='product_info_tabs_categories']/span")).click();
    	 new WebDriverWait(magneto, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div/li/ul/li[3]/div/input")));
    	 magneto.findElement(By.xpath("//div/li/ul/li[3]/div/input")).click();
    	 new WebDriverWait(magneto, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//li[3]/ul/li/div/input")));
    	  magneto.findElement(By.xpath("//li[3]/ul/li/div/input")).click();
    	 }catch(Exception e){
    		 JOptionPane.showMessageDialog(null,
    	 			    "Error while selecting category. Please use your hand",
    	 			    "Instruction",
    	 			    JOptionPane.WARNING_MESSAGE); 
    	 }
    	 try{
	    	 magneto.findElement(By.xpath("//a[@id='product_info_tabs_group_7']/span")).click();
	    	 uploadImages(data);
    	 }catch(Exception e){}
    	 
    	 magneto.findElement(By.xpath("//div[@id='content']/div/div/div/p/button[5]")).click();
    	 }catch(Exception e){
    		 e.printStackTrace();
    		 JOptionPane.showMessageDialog(null,
 	 			    "Some unknown error. Please use your hand",
 	 			    "Instruction",
 	 			    JOptionPane.WARNING_MESSAGE); 
    	 }
    	 
    	 
     }
     
     Writer writer;
     public void search(){
//    	 try{
//    	  FileOutputStream fos = new FileOutputStream(new File(writeFileName));
//    	  writer= new OutputStreamWriter(fos, "TIS-620");
//    	 }catch(Exception e){}
    	 signIn();
    	 for(int j=1;j<=7;j++){    		 
    		 System.out.println("Reading Page Number: "+j);
    		 driver.get(url+j);
    		 
    		 for(int k=1;k<=32;k++){
    			 if(k==1){ 
			    	 String productURL=driver.findElement(By.xpath("//section/ul/li/div/a")).getAttribute("href"); 
			    	 getProduct(productURL);
    			 }else{  
  					try{
  						String productURL=driver.findElement(By.xpath("//section/ul/li["+k+"]/div/a")).getAttribute("href"); 
  						 getProduct(productURL);
  					}catch(Exception e){
  						String productURL=driver.findElement(By.xpath("//section/ul/li["+k+"]/div/a")).getAttribute("href"); 
  						 getProduct(productURL);
  					}
    				 
    			 }
    		 }
    		  
    	 }
    	 
    	 
     }
     CsvWriter csvOutput; 
     public void writeCSV(Map<String,String> data){
    	 boolean alreadyExists = new File(writeFileName).exists();
		  try{
			
			  	 
					 csvOutput = new CsvWriter(new FileWriter(writeFileName, true), ',');
					if (!alreadyExists)
					{
						csvOutput.write("Product Name");
						csvOutput.write("Product Price");
						csvOutput.write("Product Size");
						csvOutput.write("Product Despcription");
						csvOutput.write("Product Image");
						csvOutput.endRecord();
					
			        }
					csvOutput.write(data.get("productName"));
					csvOutput.write(data.get("productPrice"));
					csvOutput.write(data.get("productSize"));
					csvOutput.write(data.get("productDescription"));
					csvOutput.write(data.get("productImage"));
					csvOutput.endRecord(); 
					System.out.print("Data Successfuly Written");
		  }
		  catch(Exception e){
		  }
		  }
			     
     public  void loadDriver(){
    	 System.out.println("Loading Browser");
    	 driver=new FirefoxDriver();
    	 driver2= new FirefoxDriver();
    	 magneto= new FirefoxDriver();
    	 System.out.println("Browsers are  ready");
     }
     
     public static void main(String[]args){
    	  LazadaScraper obj=new LazadaScraper();
    	  obj.loadDriver();
    	  obj.search();
    	  
     }
}
