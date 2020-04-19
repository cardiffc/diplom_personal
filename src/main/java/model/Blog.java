package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
public class Blog {

      @Value("${blog.title}")
      private String title;

      @Value("${blog.subtitle}")
      private String subtitle;

      @Value("${blog.phone}")
      private String phone;

      @Value("${blog.email}")
      private String email;

      @Value("${blog.copyright}")
      private String copyright;

      @Value("${blog.copyrightFrom}")
      private String copyrightFrom;

//      public Blog(String title, String subtitle, String phone, String email, String copyright, String copyrightFrom) {
//            this.title = title;
//            this.subtitle = subtitle;
//            this.phone = phone;
//            this.email = email;
//            this.copyright = copyright;
//            this.copyrightFrom = copyrightFrom;
//      }

//      public String getTitle() {
//            return title;
//      }
//
//      public void setTitle(String title) {
//            this.title = title;
//      }
//
//      public String getSubtitle() {
//            return subtitle;
//      }
//
//      public void setSubtitle(String subtitle) {
//            this.subtitle = subtitle;
//      }
//
//      public String getPhone() {
//            return phone;
//      }
//
//      public void setPhone(String phone) {
//            this.phone = phone;
//      }
//
//      public String getEmail() {
//            return email;
//      }
//
//      public void setEmail(String email) {
//            this.email = email;
//      }
//
//      public String getCopyright() {
//            return copyright;
//      }
//
//      public void setCopyright(String copyright) {
//            this.copyright = copyright;
//      }
//
//      public String getCopyrightFrom() {
//            return copyrightFrom;
//      }
//
//      public void setCopyrightFrom(String copyrightFrom) {
//            this.copyrightFrom = copyrightFrom;
//      }
}

