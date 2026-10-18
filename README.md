# Hazrat Amin — Portfolio App

A modern, fully working Android portfolio app with three sections — **About, Services, Contact** —
bottom navigation, splash screen, light/dark mode, interactive service cards with a detail page,
and a contact form that **really sends** (no fake success messages).

Built with the Android framework in Java (no external libraries), so it is tiny (~84 KB),
fast, and builds anywhere. All layouts are XML, all colours live in `res/values` +
`res/values-night` (that pair is what makes dark mode work).

## What's inside

| Feature | How |
|---|---|
| Splash screen | `SplashActivity` — emerald screen, HA logo, fade-in, then Main |
| Bottom navigation | About / Services / Contact tabs in `MainActivity`, selected tab highlighted, form data survives tab switches |
| About | Hero card with photo + welcome, bio, stats, skills, mission & vision, social buttons (Facebook, YouTube, Website, Fiverr) |
| Services | 6 service cards (icon, category, description) — tap a card or **Learn More** for the detail page, **Get Service** jumps to the contact form with the subject pre-filled |
| Service detail | `ServiceDetailActivity` — full description, feature list, price line, **Get This Service** button |
| Contact form | Name, email, phone, subject, message with field validation and inline errors; real HTTP POST to FormSubmit; success is shown **only** when the server replies HTTP 200 with `success: true`; progress spinner + real error messages otherwise |
| Quick contact | WhatsApp button, "Email App" button (opens the visitor's mail app pre-filled), Facebook/YouTube buttons, address shown |
| Dark mode | 🌗 button in the top bar toggles light/dark (default: follow the phone's system setting) |
| Accessibility | Content descriptions on icon buttons/tabs, large touch targets, readable contrast in both modes |

## Personal details — edit in ONE place

`app/src/main/java/io/github/aminkhan90dhup/hazratportfolio/Config.java`

- `EMAIL` — where contact-form messages arrive (also the FormSubmit form owner)
- `WHATSAPP_NUMBER` — international format, digits only (e.g. `923001234567`).
  Empty = the WhatsApp button simply opens the WhatsApp app.
- Facebook / YouTube / Website / Fiverr URLs, name, location.

Services are data, not layouts: edit `ServiceItem.all()` to add/change services.

## ⚠️ One-time activation (required once)

The contact form uses **FormSubmit** (free, no account, no API key):
`https://formsubmit.co/ajax/<your email>`.

The first submission makes FormSubmit email an **"Activate Form"** link to that inbox.
**Click it once** — from then on, every app message lands in that Gmail inbox.
(An activation email was already triggered during setup on 15 Oct 2026 — check
aminhazrat3@gmail.com for a message from FormSubmit and click **Activate Form**.)

Before activation, the app honestly shows FormSubmit's "needs activation" reply
instead of pretending the message was sent.

## Open in Android Studio

1. Android Studio → **Open** → select this `HazratPortfolio` folder.
2. Let Gradle sync (AGP 8.5.2, compileSdk 34, minSdk 24 — Android 7.0+).
3. Run ▶ on a device/emulator. There are **no third-party dependencies**.

## Generate a signed APK

- Android Studio: **Build → Generate Signed App Bundle / APK** → create or choose
  a keystore → release → APK. (Install each new version over the old one only if it
  is signed with the SAME keystore.)
- Or use the included manual pipeline (Linux, no Gradle daemon needed):
  `../build-portfolio.sh` — compiles resources/code, signs with Hazrat's release
  keystore and writes `../HazratPortfolio.apk`.

## External services used

| Service | Purpose | Cost / key |
|---|---|---|
| FormSubmit (`formsubmit.co`) | Delivers contact-form messages to Gmail | Free, no key; one-time activation click |
| WhatsApp / mail apps | Quick-contact actions on the visitor's phone | — |

No API keys or secrets exist in this app. The FormSubmit endpoint contains only the
public contact email.

## Package

`io.github.aminkhan90dhup.hazratportfolio` • v1.0 (versionCode 1) • min Android 7.0
