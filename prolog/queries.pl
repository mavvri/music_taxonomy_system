% Consultas especificas para la interfaz grafica

% Inicializacion del sistema
inicializar_sistema :-
    write('Sistema de taxonomia musical inicializado'), nl,
    total_generos(Total),
    format('Total de géneros en la base de conocimiento: ~w~n', [Total]).

% Consulta para obtener todos los generos
obtener_todos_los_generos(Generos) :-
    findall(Genero, frame(Genero, _, _, _), GenerosList),
    sort(GenerosList, Generos).

% Consulta para obtener información completa de un género
info_completa_genero(Genero, Info) :-
    frame(Genero, subclase_de(Padre), Propiedades, Descripcion),
    todas_las_propiedades(Genero, PropiedadesCompletas),
    jerarquia_clase(Genero, Jerarquia),
    subgeneros_de(Genero, Subgeneros),
    Info = genero(Genero, Padre, Propiedades, PropiedadesCompletas, Descripcion, Jerarquia, Subgeneros).

% Consulta para búsqueda por multiples criterios
busqueda_avanzada(Criterios, Resultados) :-
    obtener_todos_los_generos(TodosGeneros),
    include(cumple_criterios(Criterios), TodosGeneros, Resultados).

% Auxiliar para verificar si un genero cumple con los criterios
cumple_criterios([], _) :- !.
cumple_criterios([Criterio|RestoCriterios], Genero) :-
    cumple_criterio(Criterio, Genero),
    cumple_criterios(RestoCriterios, Genero).

cumple_criterio(instrumento(Instrumento), Genero) :-
    hereda_propiedad(Genero, usa(Instrumento)).

cumple_criterio(tempo_min(Min), Genero) :-
    hereda_propiedad(Genero, tempo_bpm(Tempo)),
    (   integer(Tempo) -> Tempo >= Min
    ;   compound(Tempo) -> 
        Tempo =.. ['_', TempoMin, _],
        TempoMin >= Min
    ).

cumple_criterio(tempo_max(Max), Genero) :-
    hereda_propiedad(Genero, tempo_bpm(Tempo)),
    (   integer(Tempo) -> Tempo =< Max
    ;   compound(Tempo) -> 
        Tempo =.. ['_', _, TempoMax],
        TempoMax =< Max
    ).

cumple_criterio(origen_pais(Pais), Genero) :-
    hereda_propiedad(Genero, origen_pais(Pais)).

cumple_criterio(caracteristica(Caracteristica), Genero) :-
    hereda_propiedad(Genero, caracteristica(Caracteristica)).

% Consulta para exportar datos en formato JSON-like para Python
exportar_datos_json(DatosJSON) :-
    findall(genero_json(Nombre, Padre, Propiedades, Descripcion), 
            frame(Nombre, subclase_de(Padre), Propiedades, Descripcion), 
            DatosJSON).

% Consulta para obtener sugerencias de búsqueda
obtener_sugerencias(Tipo, Sugerencias) :-
    (   Tipo = instrumentos ->
        findall(Instrumento, (
            frame(_, _, Propiedades, _),
            member(usa(Instrumento), Propiedades)
        ), Lista)
    ;   Tipo = paises ->
        findall(Pais, (
            frame(_, _, Propiedades, _),
            member(origen_pais(Pais), Propiedades)
        ), Lista)
    ;   Tipo = caracteristicas ->
        findall(Caracteristica, (
            frame(_, _, Propiedades, _),
            member(caracteristica(Caracteristica), Propiedades)
        ), Lista)
    ;   Tipo = decadas ->
        findall(Decada, (
            frame(_, _, Propiedades, _),
            member(origen_decada(Decada), Propiedades)
        ), Lista)
    ),
    sort(Lista, Sugerencias).

% Predicado auxiliar para obtener información mas detallada de un género
info_detallada_genero(Genero, InfoDetallada) :-
    frame(Genero, Padre, Propiedades, Descripcion),
    todas_las_propiedades(Genero, PropiedadesCompletas),
    jerarquia_clase(Genero, Jerarquia),
    subgeneros_de(Genero, Subgeneros),
    InfoDetallada = [
        nombre(Genero),
        padre(Padre),
        propiedades_directas(Propiedades),
        propiedades_heredadas(PropiedadesCompletas),
        descripcion(Descripcion),
        jerarquia(Jerarquia),
        subgeneros(Subgeneros)
    ].

% Consultas especificas para la interfaz grafica con formato legible

:- discontiguous inicializar_sistema/0.

% Inicializacion del sistema
inicializar_sistema :-
    write('Sistema de taxonomia musical inicializado'), nl,
    total_generos(Total),
    format('Total de generos en la base de conocimiento: ~w~n', [Total]).

% Obtener todos los generos
obtener_todos_los_generos(Generos) :-
    findall(Genero, frame(Genero, _, _, _), GenerosList),
    sort(GenerosList, Generos).

% Formatear propiedades para mostrar al usuario
formatear_propiedad(usa(Instrumento), Formatted) :-
    reemplazar_guiones(Instrumento, InstrumentoLimpio),
    atom_concat('Usa instrumento: ', InstrumentoLimpio, Formatted).

formatear_propiedad(origen_pais(Pais), Formatted) :-
    reemplazar_guiones(Pais, PaisLimpio),
    atom_concat('Origen pais: ', PaisLimpio, Formatted).

formatear_propiedad(origen_decada(Decada), Formatted) :-
    atom_concat('Origen decada: ', Decada, Formatted).

formatear_propiedad(origen_ciudad(Ciudad), Formatted) :-
    reemplazar_guiones(Ciudad, CiudadLimpia),
    atom_concat('Origen ciudad: ', CiudadLimpia, Formatted).

formatear_propiedad(caracteristica(Caracteristica), Formatted) :-
    reemplazar_guiones(Caracteristica, CaracteristicaLimpia),
    atom_concat('Caracteristica: ', CaracteristicaLimpia, Formatted).

formatear_propiedad(tempo_bpm(Tempo), Formatted) :-
    atom_concat('Tempo BPM: ', Tempo, Formatted).

formatear_propiedad(tempo_max(TempoMax), Formatted) :-
    atom_concat('Tempo maximo: ', TempoMax, Formatted).

formatear_propiedad(energia(Nivel), Formatted) :-
    reemplazar_guiones(Nivel, NivelLimpio),
    atom_concat('Nivel de energia: ', NivelLimpio, Formatted).

formatear_propiedad(complejidad(Nivel), Formatted) :-
    reemplazar_guiones(Nivel, NivelLimpio),
    atom_concat('Complejidad: ', NivelLimpio, Formatted).

formatear_propiedad(duracion(Duracion), Formatted) :-
    reemplazar_guiones(Duracion, DuracionLimpia),
    atom_concat('Duracion: ', DuracionLimpia, Formatted).

formatear_propiedad(volumen(Volumen), Formatted) :-
    atom_concat('Volumen: ', Volumen, Formatted).

formatear_propiedad(estructura(Estructura), Formatted) :-
    reemplazar_guiones(Estructura, EstructuraLimpia),
    atom_concat('Estructura: ', EstructuraLimpia, Formatted).

formatear_propiedad(publico(Publico), Formatted) :-
    atom_concat('Publico objetivo: ', Publico, Formatted).

formatear_propiedad(mensaje(Mensaje), Formatted) :-
    atom_concat('Tipo de mensaje: ', Mensaje, Formatted).

formatear_propiedad(actitud(Actitud), Formatted) :-
    reemplazar_guiones(Actitud, ActitudLimpia),
    atom_concat('Actitud: ', ActitudLimpia, Formatted).

formatear_propiedad(produccion(Produccion), Formatted) :-
    reemplazar_guiones(Produccion, ProduccionLimpia),
    atom_concat('Tipo de produccion: ', ProduccionLimpia, Formatted).

formatear_propiedad(fusion(Fusion), Formatted) :-
    reemplazar_guiones(Fusion, FusionLimpia),
    atom_concat('Fusion de estilos: ', FusionLimpia, Formatted).

formatear_propiedad(influencia(Influencia), Formatted) :-
    reemplazar_guiones(Influencia, InfluenciaLimpia),
    atom_concat('Influencia de: ', InfluenciaLimpia, Formatted).

formatear_propiedad(cultura(Cultura), Formatted) :-
    reemplazar_guiones(Cultura, CulturaLimpia),
    atom_concat('Cultura asociada: ', CulturaLimpia, Formatted).

formatear_propiedad(autenticidad(Nivel), Formatted) :-
    reemplazar_guiones(Nivel, NivelLimpio),
    atom_concat('Nivel de autenticidad: ', NivelLimpio, Formatted).

formatear_propiedad(innovacion(Nivel), Formatted) :-
    atom_concat('Nivel de innovacion: ', Nivel, Formatted).

formatear_propiedad(tradicion(Tipo), Formatted) :-
    atom_concat('Tradicion: ', Tipo, Formatted).

formatear_propiedad(estilo(Estilo), Formatted) :-
    reemplazar_guiones(Estilo, EstiloLimpio),
    atom_concat('Estilo: ', EstiloLimpio, Formatted).

formatear_propiedad(funcion(Funcion), Formatted) :-
    atom_concat('Funcion: ', Funcion, Formatted).

formatear_propiedad(lider(Lider), Formatted) :-
    reemplazar_guiones(Lider, LiderLimpio),
    atom_concat('Lider del movimiento: ', LiderLimpio, Formatted).

formatear_propiedad(emocion(Emocion), Formatted) :-
    atom_concat('Emocion principal: ', Emocion, Formatted).

formatear_propiedad(tema(Tema), Formatted) :-
    atom_concat('Tematica: ', Tema, Formatted).

formatear_propiedad(accesibilidad(Nivel), Formatted) :-
    atom_concat('Accesibilidad: ', Nivel, Formatted).

formatear_propiedad(diversidad(Nivel), Formatted) :-
    atom_concat('Diversidad: ', Nivel, Formatted).

formatear_propiedad(comercialidad(Nivel), Formatted) :-
    atom_concat('Comercialidad: ', Nivel, Formatted).

formatear_propiedad(creatividad(Nivel), Formatted) :-
    atom_concat('Creatividad: ', Nivel, Formatted).

formatear_propiedad(expresion(Tipo), Formatted) :-
    atom_concat('Expresion: ', Tipo, Formatted).

formatear_propiedad(instrumentos(Tipo), Formatted) :-
    atom_concat('Instrumentos: ', Tipo, Formatted).

formatear_propiedad(origen_region(Region), Formatted) :-
    atom_concat('Region de origen: ', Region, Formatted).

formatear_propiedad(origen_periodo(Periodo), Formatted) :-
    atom_concat('Periodo de origen: ', Periodo, Formatted).

formatear_propiedad(transmision(Tipo), Formatted) :-
    atom_concat('Transmision: ', Tipo, Formatted).

formatear_propiedad(origen(Tipo), Formatted) :-
    atom_concat('Origen: ', Tipo, Formatted).

formatear_propiedad(necesita(Cosa), Formatted) :-
    atom_concat('Necesita: ', Cosa, Formatted).

formatear_propiedad(tiene(Cosa), Formatted) :-
    atom_concat('Tiene: ', Cosa, Formatted).

formatear_propiedad(es_arte(true), 'Es considerado arte').

% Para propiedades que no tienen formato especifico
formatear_propiedad(Propiedad, Formatted) :-
    \+ compound(Propiedad),
    reemplazar_guiones(Propiedad, PropiedadLimpia),
    atom_concat('Propiedad: ', PropiedadLimpia, Formatted).

formatear_propiedad(Propiedad, Formatted) :-
    compound(Propiedad),
    term_to_atom(Propiedad, AtomProp),
    reemplazar_guiones(AtomProp, PropLimpia),
    atom_concat('Caracteristica: ', PropLimpia, Formatted).

% Auxiliar para reemplazar guiones bajos con espacios
reemplazar_guiones(Atom, AtomLimpio) :-
    atom_codes(Atom, Codes),
    reemplazar_guiones_codes(Codes, CodesLimpios),
    atom_codes(AtomLimpio, CodesLimpios).

reemplazar_guiones_codes([], []).
reemplazar_guiones_codes([95|Rest], [32|RestLimpio]) :- % 95 = '_', 32 = ' '
    reemplazar_guiones_codes(Rest, RestLimpio).
reemplazar_guiones_codes([Code|Rest], [Code|RestLimpio]) :-
    Code \= 95,
    reemplazar_guiones_codes(Rest, RestLimpio).

% Obtener propiedades formateadas para un genero
propiedades_formateadas(Genero, PropiedadesFormateadas) :-
    todas_las_propiedades(Genero, Propiedades),
    maplist(formatear_propiedad, Propiedades, PropiedadesFormateadas).

% Predicados de compatibilidad con la interfaz Java existente
clases(L) :-
    obtener_todos_los_generos(L).

propiedadesc(Clase, L) :-
    propiedades_formateadas(Clase, L).

superclases_de(Clase, L) :-
    jerarquia_clase(Clase, L).

todas_propiedades(L) :-
    findall(P, (frame(_, _, Props, _), member(P, Props)), AllProps),
    sort(AllProps, L).

obtiene_descripcion(Clase, Desc) :-
    frame(Clase, _, _, Desc).

tiene_propiedad(Propiedad, Clases) :-
    % Try direct property match first
    findall(Clase, (
        frame(Clase, _, Props, _),
        member(Propiedad, Props)
    ), ClasesList1),
    % Try parsing property if it's an atom
    (   atom(Propiedad) ->
        (   atom_to_term(Propiedad, PropTerm, []) ->
            findall(Clase, (
                frame(Clase, _, Props, _),
                member(PropTerm, Props)
            ), ClasesList2)
        ;   ClasesList2 = []
        )
    ;   ClasesList2 = []
    ),
    append(ClasesList1, ClasesList2, AllClases),
    sort(AllClases, Clases).

% Alternative property search for formatted properties
buscar_propiedad_formateada(PropiedadTexto, Clases) :-
    % Extract property components
    (   atom_concat('accesibilidad(', Resto, PropiedadTexto),
        atom_concat(Valor, ')', Resto) ->
        PropiedadReal = accesibilidad(Valor)
    ;   atom_concat('estructura(', Resto, PropiedadTexto),
        atom_concat(Valor, ')', Resto) ->
        PropiedadReal = estructura(Valor)
    ;   atom_concat('origen_decada(', Resto, PropiedadTexto),
        atom_concat(ValorAtom, ')', Resto),
        atom_number(ValorAtom, Valor) ->
        PropiedadReal = origen_decada(Valor)
    ;   atom_concat('origen_region(', Resto, PropiedadTexto),
        atom_concat(Valor, ')', Resto) ->
        PropiedadReal = origen_region(Valor)
    ;   atom_concat('tempo_bpm(', Resto, PropiedadTexto),
        atom_concat(ValorAtom, ')', Resto),
        atom_number(ValorAtom, Valor) ->
        PropiedadReal = tempo_bpm(Valor)
    ;   atom_concat('usa(', Resto, PropiedadTexto),
        atom_concat(Valor, ')', Resto) ->
        PropiedadReal = usa(Valor)
    ;   atom_concat('caracteristica(', Resto, PropiedadTexto),
        atom_concat(Valor, ')', Resto) ->
        PropiedadReal = caracteristica(Valor)
    ;   atom_concat('origen_pais(', Resto, PropiedadTexto),
        atom_concat(Valor, ')', Resto) ->
        PropiedadReal = origen_pais(Valor)
    ;   PropiedadReal = PropiedadTexto
    ),
    findall(Clase, (
        frame(Clase, _, Props, _),
        member(PropiedadReal, Props)
    ), ClasesList),
    sort(ClasesList, Clases).