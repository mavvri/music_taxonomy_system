% Base de conocimiento ampliada de generos musicales usando frames
% Estructura: frame(Nombre, subclase_de(Padre), [Lista_Propiedades], 'Texto_Descripcion')

% Frame raiz - Musica
frame(musica, subclase_de(top),
    [es_arte(true), necesita(sonido), tiene(ritmo), tiene(melodia)],
    'La musica es el arte de combinar sonidos y silencios en el tiempo').

% === GENEROS PRINCIPALES ===

% Musica Electronica
frame(musica_electronica, subclase_de(musica),
    [usa(sintetizadores), usa(computadora), origen_decada(1970), caracteristica(digital), produccion(electronica)],
    'Musica producida principalmente con instrumentos electronicos y tecnologia digital').

% Rock
frame(rock, subclase_de(musica),
    [usa(guitarra_electrica), usa(bateria), usa(bajo_electrico), origen_decada(1950), caracteristica(amplificado), energia(alta)],
    'Genero musical caracterizado por guitarras electricas, ritmo fuerte y actitud rebelde').

% Jazz
frame(jazz, subclase_de(musica),
    [caracteristica(improvisacion), usa(instrumentos_viento), origen_pais(usa), origen_decada(1910), complejidad(alta)],
    'Genero musical que se caracteriza por la improvisacion, ritmos sincopados y armonias complejas').

% Musica Clasica
frame(musica_clasica, subclase_de(musica),
    [usa(orquesta), caracteristica(formal), origen_periodo(barroco), duracion(larga), complejidad(muy_alta)],
    'Tradicion musical culta europea que abarca desde el periodo medieval hasta la actualidad').

% Folk
frame(folk, subclase_de(musica),
    [caracteristica(tradicional), transmision(oral), usa(instrumentos_acusticos), origen(popular), autenticidad(alta)],
    'Musica tradicional transmitida oralmente de generacion en generacion').

% Pop
frame(pop, subclase_de(musica),
    [caracteristica(comercial), duracion(tres_cuatro_minutos), estructura(verso_coro), publico(masivo), accesibilidad(alta)],
    'Musica popular disenada para tener amplio atractivo comercial').

% Reggae
frame(reggae, subclase_de(musica),
    [origen_pais(jamaica), caracteristica(offbeat), usa(guitarra_ritmica), origen_decada(1960), mensaje(social)],
    'Genero musical originario de Jamaica caracterizado por su ritmo distintivo').

% Hip Hop
frame(hip_hop, subclase_de(musica),
    [usa(turntables), caracteristica(sampling), origen_pais(usa), origen_decada(1970), cultura(urbana)],
    'Movimiento cultural urbano que incluye rap, DJing, breaking y graffiti').

% R&B
frame(rhythm_and_blues, subclase_de(musica),
    [usa(voz_soul), origen_pais(usa), origen_decada(1940), influencia(gospel), emocion(alta)],
    'Genero musical afroamericano que combina blues, jazz y gospel').

% Country
frame(country, subclase_de(musica),
    [usa(guitarra_acustica), usa(violin), origen_pais(usa), origen_decada(1920), tema(rural)],
    'Musica popular rural estadounidense con raices en folk, blues y musica country occidental').

% Blues
frame(blues, subclase_de(musica),
    [estructura(doce_compases), usa(guitarra_blues), origen_pais(usa), origen_decada(1860), emocion(melancolia)],
    'Genero musical afroamericano caracterizado por la escala blues y expresion emocional').

% === SUBGENEROS DE MUSICA ELECTRONICA ===

frame(house, subclase_de(musica_electronica),
    [tempo_bpm(120), tempo_max(130), origen_ciudad(chicago), usa(drum_machine), caracteristica(repetitivo), energia(media)],
    'Subgenero de musica electronica con ritmo de cuatro por cuatro y beats repetitivos').

frame(techno, subclase_de(musica_electronica),
    [tempo_bpm(120), tempo_max(150), origen_ciudad(detroit), caracteristica(industrial), usa(secuenciador), energia(alta)],
    'Musica electronica de baile con sonidos industriales y ritmos mecanicos').

frame(ambient, subclase_de(musica_electronica),
    [tempo_bpm(60), tempo_max(90), caracteristica(atmosferico), funcion(relajacion), estructura(libre), energia(baja)],
    'Musica electronica que enfatiza el ambiente y la atmosfera sobre la estructura tradicional').

frame(dubstep, subclase_de(musica_electronica),
    [tempo_bpm(140), caracteristica(wobble_bass), origen_pais(reino_unido), origen_decada(2000), energia(muy_alta)],
    'Genero de musica electronica caracterizado por ritmos sincopados y bajos prominentes').

frame(trance, subclase_de(musica_electronica),
    [tempo_bpm(125), tempo_max(150), caracteristica(hipnotico), estructura(buildup_drop), duracion(larga), energia(alta)],
    'Musica electronica repetitiva disenada para inducir estados de trance').

frame(drum_and_bass, subclase_de(musica_electronica),
    [tempo_bpm(160), tempo_max(180), caracteristica(breakbeats), origen_pais(reino_unido), origen_decada(1990), complejidad(alta)],
    'Genero electronico caracterizado por breakbeats rapidos y lineas de bajo pesadas').

frame(electro_swing, subclase_de(musica_electronica),
    [fusion(jazz_electronica), caracteristica(vintage), origen_decada(2000), instrumentos(mixtos), estilo(retro_futurista)],
    'Fusion de musica electronica con swing y jazz de la era de 1920-1940').

% === SUBGENEROS DE ROCK ===

frame(heavy_metal, subclase_de(rock),
    [volumen(alto), usa(guitarra_distorsionada), caracteristica(agresivo), tempo_bpm(100), tempo_max(200), energia(muy_alta)],
    'Subgenero del rock caracterizado por guitarras pesadas y sonido potente').

frame(punk_rock, subclase_de(rock),
    [duracion(corta), caracteristica(crudo), tempo_bpm(150), tempo_max(200), actitud(rebelde), mensaje(protesta)],
    'Genero musical rapido, crudo y con letras directas de protesta social').

frame(rock_alternativo, subclase_de(rock),
    [caracteristica(experimental), origen_decada(1980), publico(underground), diversidad(alta), comercialidad(baja)],
    'Movimiento musical que emergio como alternativa al rock mainstream').

frame(rock_progresivo, subclase_de(rock),
    [duracion(larga), estructura(compleja), usa(sintetizadores), caracteristica(conceptual), complejidad(muy_alta)],
    'Subgenero que incorpora elementos de jazz, clasica y musica experimental').

frame(grunge, subclase_de(rock),
    [origen_ciudad(seattle), origen_decada(1980), caracteristica(distorsionado), actitud(apatica), energia(media_alta)],
    'Subgenero del rock alternativo con guitarras distorsionadas y letras angustiadas').

frame(indie_rock, subclase_de(rock),
    [produccion(independiente), caracteristica(experimental), publico(nicho), origen_decada(1980), creatividad(alta)],
    'Rock producido independientemente con enfasis en la creatividad artistica').

% === SUBGENEROS DE JAZZ ===

frame(bebop, subclase_de(jazz),
    [tempo_bpm(120), tempo_max(300), caracteristica(virtuoso), estructura(improvisacion_compleja), origen_decada(1940), complejidad(muy_alta)],
    'Estilo de jazz caracterizado por tempos rapidos y improvisacion compleja').

frame(smooth_jazz, subclase_de(jazz),
    [tempo_bpm(60), tempo_max(120), caracteristica(suave), usa(saxofon), publico(comercial), accesibilidad(alta)],
    'Subgenero comercial del jazz con melodias suaves y accesibles').

frame(jazz_fusion, subclase_de(jazz),
    [usa(instrumentos_electricos), caracteristica(hibridacion), origen_decada(1960), influencia(rock), innovacion(alta)],
    'Fusion del jazz con elementos de rock, funk y musica electronica').

frame(swing, subclase_de(jazz),
    [tempo_bpm(120), tempo_max(180), caracteristica(bailable), origen_decada(1930), usa(big_band), energia(alta)],
    'Estilo de jazz con ritmo marcado ideal para el baile').

% === SUBGENEROS DE HIP HOP ===

frame(rap, subclase_de(hip_hop),
    [usa(voz_rapping), caracteristica(ritmica), mensaje(narrativo), origen_decada(1970), expresion(verbal)],
    'Forma de expresion vocal ritmica que es el elemento central del hip hop').

frame(trap, subclase_de(hip_hop),
    [usa(drums_808), caracteristica(dark), origen_pais(usa), origen_decada(2000), energia(alta)],
    'Subgenero del hip hop con beats pesados y tematica urbana').

frame(old_school_hip_hop, subclase_de(hip_hop),
    [origen_decada(1970), caracteristica(original), usa(breakbeats), cultura(block_party), autenticidad(muy_alta)],
    'Primera generacion del hip hop con enfasis en la cultura de barrio').

% === OTROS SUBGENEROS ===

frame(reggaeton, subclase_de(reggae),
    [tempo_bpm(90), tempo_max(100), origen_pais(puerto_rico), usa(dembow), caracteristica(urbano), energia(alta)],
    'Genero musical urbano que combina reggae con hip hop y musica latina').

frame(indie_pop, subclase_de(pop),
    [caracteristica(independiente), produccion(casera), publico(nicho), actitud(alternativa), creatividad(alta)],
    'Musica pop producida independientemente con estetica alternativa').

frame(bluegrass, subclase_de(folk),
    [usa(banjo), usa(violin), origen_pais(usa), caracteristica(virtuoso), tradicion(americana)],
    'Estilo de musica folk americana caracterizada por instrumentacion acustica virtuosa').

frame(neo_soul, subclase_de(rhythm_and_blues),
    [origen_decada(1990), caracteristica(contemporaneo), influencia(hip_hop), emocion(profunda), innovacion(alta)],
    'Evolucion moderna del soul clasico con influencias contemporaneas').

frame(delta_blues, subclase_de(blues),
    [origen_region(mississippi), caracteristica(acustico), estilo(primitivo), autenticidad(muy_alta), origen_decada(1920)],
    'Estilo primitivo de blues del delta del Mississippi').

frame(bluegrass_progressive, subclase_de(bluegrass),
    [caracteristica(experimental), influencia(jazz), complejidad(alta), innovacion(alta), tradicion(moderna)],
    'Evolucion del bluegrass que incorpora elementos de jazz y rock').

% === GENEROS ADICIONALES ===

frame(funk, subclase_de(musica),
    [caracteristica(groove), usa(bajo_electrico), origen_pais(usa), origen_decada(1960), energia(muy_alta)],
    'Genero musical centrado en un ritmo danceable fuerte y repetitivo').

frame(disco, subclase_de(musica),
    [caracteristica(bailable), tempo_bpm(100), tempo_max(130), origen_decada(1970), publico(masivo), energia(alta)],
    'Genero de musica dance que domino las pistas de baile en los anos 70').

frame(ska, subclase_de(musica),
    [usa(instrumentos_viento), origen_pais(jamaica), caracteristica(upbeat), origen_decada(1950), energia(alta)],
    'Precursor del reggae con ritmo acelerado y seccion de vientos prominente').

% Subgenero de Funk
frame(p_funk, subclase_de(funk),
    [caracteristica(psicodelico), lider(george_clinton), origen_decada(1970), estilo(experimental), energia(muy_alta)],
    'Variante psicodelica del funk liderada por George Clinton').

% === HECHOS AUXILIARES ===
instrumento(guitarra_electrica).
instrumento(sintetizadores).
instrumento(bateria).
instrumento(saxofon).
instrumento(banjo).
instrumento(violin).
instrumento(piano).
instrumento(bajo_electrico).
instrumento(turntables).
instrumento(drum_machine).
instrumento(secuenciador).
instrumento(instrumentos_viento).
instrumento(orquesta).
instrumento(guitarra_acustica).
instrumento(guitarra_ritmica).
instrumento(voz_soul).
instrumento(voz_rapping).
instrumento(drums_808).

pais(usa).
pais(jamaica).
pais(reino_unido).
pais(puerto_rico).

ciudad(chicago).
ciudad(detroit).
ciudad(seattle).

decada(1860).
decada(1910).
decada(1920).
decada(1930).
decada(1940).
decada(1950).
decada(1960).
decada(1970).
decada(1980).
decada(1990).
decada(2000).

caracteristica_musical(digital).
caracteristica_musical(amplificado).
caracteristica_musical(improvisacion).
caracteristica_musical(formal).
caracteristica_musical(tradicional).
caracteristica_musical(comercial).
caracteristica_musical(offbeat).
caracteristica_musical(sampling).
caracteristica_musical(repetitivo).
caracteristica_musical(industrial).
caracteristica_musical(atmosferico).
caracteristica_musical(hipnotico).
caracteristica_musical(agresivo).
caracteristica_musical(crudo).
caracteristica_musical(experimental).
caracteristica_musical(conceptual).
caracteristica_musical(virtuoso).
caracteristica_musical(suave).
caracteristica_musical(hibridacion).
caracteristica_musical(urbano).
caracteristica_musical(independiente).
caracteristica_musical(groove).
caracteristica_musical(bailable).
caracteristica_musical(upbeat).
caracteristica_musical(psicodelico).