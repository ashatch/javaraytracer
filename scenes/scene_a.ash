optics:light {
    colour 1.0 1.0 1.0;
    refraction 0.0;
    transparency 0.0;
    reflection 0.5;
    diffusion 0.5;
    luminous yes;
}

optics:redsphere {
    colour 0.95 0.1 0.1;
    refraction 0.0;
    transparency 0.0;
    reflection 0.5;
    diffusion 0.4;
    luminous no;
}

optics:bluesphere {
    colour 0.1 0.1 1.0;
    refraction 0.0;
    transparency 0.0;
    reflection 0.5;
    diffusion 0.4;
    luminous no;
}

optics:greensphere {
    colour 0.1 1.0 0.1;
    refraction 0.0;
    transparency 0.0;
    reflection 0.5;
    diffusion 0.4;
    luminous no;
}

optics:purplesphere {
    colour 0.7 0.2 0.7;
    refraction 0.0;
    transparency 0.0;
    reflection 0.0;
    diffusion 0.6;
    luminous no;
}

optics:clearsphere {
    colour 1.0 1.0 1.0;
    refraction 0.98;
    transparency 0.9;
    reflection 0.6;
    diffusion 0.1;
    luminous no;
}

optics:greyfloor {
    colour 0.4 0.4 0.4;
    refraction 0.0;
    transparency 0.0;
    reflection 0.0;
    diffusion 0.4;
    luminous no;
}

Σ {
    size 1200 800;
    → 0.0 100.0 -600.000;
    lookAt 0.0 30.0 0.0;
    ambience 0.1 0.1 0.1;
    maxDepth 16;
}

☼ {
    λ light;
    → 500.0 1500.0 0.0;
    Ø 180.0;
    Γ 0.5;
}

◯ {
    λ bluesphere;
    → 80.0 0.0 800.0;
    Ø 800.0;
}

◯ {
    λ greensphere;
    → -100.0 -50.0 100.0;
    Ø 160.0;
}

◯ {
    λ purplesphere;
    → -210.0 -20.0 -20.0;
    Ø 120.0;
}

◯ {
    λ clearsphere;
    → 120.0 -40.0 -40.0;
    Ø 180.0;
}

◯ {
    λ redsphere;
    → 300.0 0.0 100.0;
    Ø 180.0;
}

▭ {
    λ greyfloor;
    → 0.0 -80.0 0.0;
}
