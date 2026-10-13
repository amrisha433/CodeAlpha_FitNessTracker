package com.example.fitnesstrackapp.ui.theme

import androidx.compose.ui.graphics.Color

// ─────────────────────────────── Dark Fitness Palette ─────────────────────────
// Deep charcoal backgrounds
val DarkBackground    = Color(0xFF0F1117)   // page background
val DarkSurface       = Color(0xFF1A1D27)   // card surface
val DarkSurfaceVar    = Color(0xFF23263A)   // elevated surface / secondary card

// Primary accent – electric blue-violet
val PrimaryBlue       = Color(0xFF5B8DEF)   // primary interactive / progress
val PrimaryBlueDim    = Color(0xFF3B6BD6)   // slightly darker for pressed states
val OnPrimary         = Color(0xFFFFFFFF)

// Secondary / containers
val SecondaryContainer = Color(0xFF1E2A45)  // secondary card fill (streaks, etc.)
val OnSecondaryContainer = Color(0xFFB0C4FF)

// Success / goal-exceeded green
val SuccessGreen      = Color(0xFF22C55E)
val SuccessGreenDim   = Color(0xFF16A34A)

// Accent teal (hydration, steps)
val AccentTeal        = Color(0xFF14B8A6)
val AccentTealDim     = Color(0xFF0F766E)

// Warning / in-progress amber
val AmberWarn         = Color(0xFFF59E0B)

// Error / destructive
val ErrorRed          = Color(0xFFEF4444)
val ErrorRedDim       = Color(0xFFDC2626)

// Text
val TextPrimary       = Color(0xFFF1F5F9)   // high-emphasis text on dark
val TextSecondary     = Color(0xFF94A3B8)   // medium-emphasis / labels
val TextDisabled      = Color(0xFF475569)   // disabled / hint

// Borders / dividers
val BorderSubtle      = Color(0xFF2D3355)

// Legacy aliases kept so existing imports don't break
val Navy              = DarkSurface
val Teal              = AccentTeal
val Green             = SuccessGreen
val Red               = ErrorRed
val CardBackground    = DarkSurface
val LightText         = TextSecondary

// Material seed colours used only as fallback if dynamic-color somehow activates
val Purple80          = Color(0xFFD0BCFF)
val PurpleGrey80      = Color(0xFFCCC2DC)
val Pink80            = Color(0xFFEFB8C8)
val Purple40          = Color(0xFF6650A4)
val PurpleGrey40      = Color(0xFF625B71)
val Pink40            = Color(0xFF7D5260)

// Kept for back-compat with existing usages in older components
val DarkText          = Color(0xFF0F172A)
val SecondaryText     = Color(0xFF64748B)
val TealPrimary       = AccentTeal
val WaterBlue         = Color(0xFF3B82F6)
val CaloriesRed       = ErrorRed
val StepsGreen        = SuccessGreen
val ProgressOrange    = AmberWarn
val PrimaryGreen      = SuccessGreen
val CardColor         = DarkSurface
val TextWhite         = Color(0xFFFFFFFF)
val AccentBlue        = PrimaryBlue