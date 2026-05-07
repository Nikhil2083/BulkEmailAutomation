package main;

import util.ExcelReader;
import util.EmailSender;
import util.ConfigReader;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BulkEmailMain {

    public static void main(String[] args) {

        List<String> emails = ExcelReader.getEmails();
     //   List<String> failedEmails = new ArrayList<>();

        // 👉 config मधून time घे
        String time = ConfigReader.get("schedule.time"); // e.g. 09:30
        String[] parts = time.split(":");

        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withHour(hour).withMinute(minute).withSecond(0);

        // 👉 जर time already गेलं असेल → उद्या schedule
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1);
        }

        long delay = Duration.between(now, nextRun).getSeconds();

        System.out.println("⏳ Emails will start at: " + nextRun);

        scheduler.schedule(() -> {

            int successCount = 0;
            int failCount = 0;
            int skipCount = 0;
            
            
            List<String> failedEmails = new ArrayList<>();
            
            int emailDelay = Integer.parseInt(ConfigReader.get("email.delay"));
            
            try {
            	
            	int mailCount = 1;

                for (String email : emails) {
                	System.out.println("\n📧 Sending Mail #" + mailCount + " to: " + email);

                	String result = EmailSender.sendEmail(email);

                	if ("SUCCESS".equals(result)) {
                        successCount++;
                    } else if ("FAILED".equals(result)) {
                        failCount++;
                        failedEmails.add(email);
                    } else {
                        skipCount++;
                    }

                	mailCount++;
                    Thread.sleep(emailDelay);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                // ✅ Final Report
                System.out.println("\n🎯 ===== FINAL REPORT =====");
                System.out.println("✅ Success: " + successCount);
                System.out.println("❌ Failed: " + failCount);
                System.out.println("⚠️ Skipped: " + skipCount);
                System.out.println("📧 Total: " + emails.size());

                // ✅ Failed list
                System.out.println("\n❌ Failed Email List:");
                if (failedEmails.isEmpty()) {
                    System.out.println("🎉 No failed emails!");
                } else {
                    for (String failed : failedEmails) {
                        System.out.println(failed);
                    }
                }

                // ✅ Summary Mail ALWAYS send होईल
                EmailSender.sendSummaryEmail(successCount, failCount, skipCount, emails.size());

                scheduler.shutdown();
                System.out.println("🛑 Execution completed. Scheduler stopped.");
            }

        }, delay, TimeUnit.SECONDS);
    }
}