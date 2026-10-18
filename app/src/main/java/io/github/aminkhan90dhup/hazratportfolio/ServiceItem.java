package io.github.aminkhan90dhup.hazratportfolio;

public class ServiceItem {
    public final String icon, title, category, shortDesc, longDesc, priceLine;
    public final String[] features;
    public ServiceItem(String icon, String title, String category, String shortDesc,
                       String longDesc, String priceLine, String[] features) {
        this.icon = icon; this.title = title; this.category = category;
        this.shortDesc = shortDesc; this.longDesc = longDesc;
        this.priceLine = priceLine; this.features = features;
    }
    public static ServiceItem[] all() {
        return new ServiceItem[] {
            new ServiceItem("🎓", "Computer Science Lectures", "Education",
                "Recorded CS/IT lectures for Classes 6–12 with full notes and exam questions.",
                "Complete Computer Science lecture series following the KP textbook syllabus. Every lecture comes with a video lesson, written notes, a full transcript and practice exam questions with answers — the same series published on my YouTube channel and website.",
                "Free on YouTube • Custom series on request",
                new String[]{"Video lectures (KP syllabus)", "Notes + full transcripts", "Exam questions with answers", "Urdu explanations"}),
            new ServiceItem("🎬", "AI Video Creation", "Video",
                "Eye-catching AI videos for YouTube, Facebook and TikTok — shorts, reels and promos.",
                "Professional AI-generated videos for social media: scenic shorts, funny family skits with real spoken Urdu dialogue, product promos and channel intros. Delivered ready to post, with a custom click-focused thumbnail.",
                "Fiverr gigs from $5 • 1–6 days delivery",
                new String[]{"Vertical shorts & reels", "Spoken Urdu dialogue videos", "Custom thumbnails", "Background music included"}),
            new ServiceItem("🌐", "Website Development", "Web",
                "Fast, free-hosted personal, portfolio and school websites — designed and maintained.",
                "Modern mobile-friendly websites built and hosted free on GitHub Pages: lectures and notes sections, daily news pages, jobs boards, photo galleries and an Android WebView app of your site. I also keep the site updated for you.",
                "Contact for a quote",
                new String[]{"Free hosting setup", "Daily content updates option", "Android app of your website", "Google search registration"}),
            new ServiceItem("📰", "Tech News & Content", "Content",
                "Daily technology & AI news, headlines and social media content for your page.",
                "A complete daily content service for Facebook pages and websites: a full tech/AI article every morning, headline updates through the day, designed Urdu quote cards and scheduled posting — the same system running on my own page and website.",
                "Monthly packages — contact for details",
                new String[]{"Daily article + 3 headline posts", "Bilingual (English + Urdu)", "Designed image cards", "Scheduled publishing"}),
            new ServiceItem("🎵", "Urdu Songs & Voiceover", "Audio",
                "Original Urdu songs, lyric videos and voiceovers for your projects.",
                "Original Urdu/Hindi songs composed and produced with AI music tools, full lyric videos with beautiful valley visuals, and clear Urdu voiceovers for lectures, ads and documentaries.",
                "Contact for a quote",
                new String[]{"Original song production", "Lyric videos", "Urdu voiceover", "Your lyrics or mine"}),
            new ServiceItem("📊", "Data Science & Math Help", "Data",
                "BS-level mathematics and data science tutoring, solved papers and project help.",
                "One-to-one help for BS Data Science and mathematics students: pre-calculus, statistics and programming concepts explained step by step, solved past papers in a clear human style, and guidance for semester projects.",
                "Per session / per paper — contact for details",
                new String[]{"Step-by-step solved papers", "Pre-calculus & statistics", "Python & data concepts", "Project guidance"}),
        };
    }
}
