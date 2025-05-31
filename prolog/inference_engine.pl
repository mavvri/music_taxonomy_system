% Motor de inferencia para el sistema de frames de géneros musicales

% Consulta si un frame es subclase de otro (transitivo)
es_subclase_de(Hijo, Padre) :-
    frame(Hijo, subclase_de(Padre), _, _).

es_subclase_de(Hijo, Ancestro) :-
    frame(Hijo, subclase_de(Padre), _, _),
    es_subclase_de(Padre, Ancestro).

% Herencia de propiedades
hereda_propiedad(Clase, Propiedad) :-
    frame(Clase, _, Propiedades, _),
    member(Propiedad, Propiedades).

hereda_propiedad(Clase, Propiedad) :-
    frame(Clase, subclase_de(Padre), _, _),
    Padre \= top,
    hereda_propiedad(Padre, Propiedad).

% Consultar todas las propiedades de una clase (incluyendo herencia)
todas_las_propiedades(Clase, TodasPropiedades) :-
    findall(Prop, hereda_propiedad(Clase, Prop), PropsList),
    sort(PropsList, TodasPropiedades).

% Buscar clases que tengan una propiedad específica
clases_con_propiedad(Propiedad, Clases) :-
    findall(Clase, hereda_propiedad(Clase, Propiedad), ClasesList),
    sort(ClasesList, Clases).

% Buscar clases por instrumento
clases_que_usan_instrumento(Instrumento, Clases) :-
    clases_con_propiedad(usa(Instrumento), Clases).

% Buscar clases por rango de tempo
clases_por_tempo(TempoMin, TempoMax, Clases) :-
    findall(Clase, (
        hereda_propiedad(Clase, tempo_bpm(Rango)),
        tempo_en_rango(Rango, TempoMin, TempoMax)
    ), ClasesList),
    sort(ClasesList, Clases).

% Auxiliar para verificar si el tempo está en rango
tempo_en_rango(Tempo, Min, Max) :-
    integer(Tempo),
    Tempo >= Min,
    Tempo =< Max.

tempo_en_rango(TempoMin_TempoMax, Min, Max) :-
    compound(TempoMin_TempoMax),
    TempoMin_TempoMax =.. ['_', TempoMin, TempoMax],
    TempoMin =< Max,
    TempoMax >= Min.

% Buscar clases por país de origen
clases_por_origen(Pais, Clases) :-
    clases_con_propiedad(origen_pais(Pais), Clases).

% Buscar clases por década
clases_por_decada(Decada, Clases) :-
    clases_con_propiedad(origen_decada(Decada), Clases).

% Buscar clases por característica
clases_por_caracteristica(Caracteristica, Clases) :-
    clases_con_propiedad(caracteristica(Caracteristica), Clases).

% Obtener descripción de una clase
descripcion_clase(Clase, Descripcion) :-
    frame(Clase, _, _, Descripcion).

% Obtener jerarquía completa de una clase
jerarquia_clase(Clase, Jerarquia) :-
    jerarquia_aux(Clase, [], Jerarquia).

jerarquia_aux(top, Acc, Acc) :- !.
jerarquia_aux(Clase, Acc, Jerarquia) :-
    frame(Clase, subclase_de(Padre), _, _),
    jerarquia_aux(Padre, [Clase|Acc], Jerarquia).

% Obtener todos los subgéneros de un género
subgeneros_de(Genero, Subgeneros) :-
    findall(Subgenero, es_subclase_de(Subgenero, Genero), SubgenerosList),
    sort(SubgenerosList, Subgeneros).

% Obtener árbol taxonómico completo
arbol_taxonomico(Arbol) :-
    findall(frame(Nombre, Padre, Propiedades, Descripcion), 
            frame(Nombre, subclase_de(Padre), Propiedades, Descripcion), 
            Arbol).

% Estadísticas del sistema
total_generos(Total) :-
    findall(Clase, frame(Clase, _, _, _), Clases),
    length(Clases, Total).

generos_por_instrumento(Estadisticas) :-
    findall(Instrumento, (
        frame(_, _, Propiedades, _),
        member(usa(Instrumento), Propiedades)
    ), InstrumentosList),
    msort(InstrumentosList, InstrumentosOrdenados),
    contar_instrumentos(InstrumentosOrdenados, Estadisticas).

% Auxiliar para contar instrumentos
contar_instrumentos([], []).
contar_instrumentos([Instrumento|Rest], [Instrumento-Count|Stats]) :-
    contar_ocurrencias(Instrumento, [Instrumento|Rest], Count, Remaining),
    contar_instrumentos(Remaining, Stats).

contar_ocurrencias(_, [], 0, []).
contar_ocurrencias(X, [X|Rest], Count, Remaining) :-
    contar_ocurrencias(X, Rest, Count1, Remaining),
    Count is Count1 + 1.
contar_ocurrencias(X, [Y|Rest], Count, [Y|Remaining]) :-
    X \= Y,
    contar_ocurrencias(X, Rest, Count, Remaining).

% Buscar clases que tengan TODAS las propiedades especificadas
clases_con_todas_propiedades([], Clases) :-
    obtener_todos_los_generos(Clases).

clases_con_todas_propiedades([Propiedad|RestoPropiedades], Clases) :-
    clases_con_propiedad(Propiedad, ClasesConEsta),
    clases_con_todas_propiedades(RestoPropiedades, ClasesConResto),
    intersection(ClasesConEsta, ClasesConResto, Clases).

% Buscar clases que tengan AL MENOS UNA de las propiedades especificadas
clases_con_alguna_propiedad([], []).
clases_con_alguna_propiedad([Propiedad|RestoPropiedades], ClasesFinales) :-
    clases_con_propiedad(Propiedad, ClasesConEsta),
    clases_con_alguna_propiedad(RestoPropiedades, ClasesConResto),
    union(ClasesConEsta, ClasesConResto, ClasesFinales).

% Verificar si una clase tiene todas las propiedades de una lista
tiene_todas_propiedades(_, []).
tiene_todas_propiedades(Clase, [Propiedad|RestoPropiedades]) :-
    hereda_propiedad(Clase, Propiedad),
    tiene_todas_propiedades(Clase, RestoPropiedades).

% Contar cuantas propiedades de una lista tiene una clase
contar_propiedades_clase(Clase, ListaPropiedades, Contador) :-
    findall(P, (member(P, ListaPropiedades), hereda_propiedad(Clase, P)), PropiedadesEncontradas),
    length(PropiedadesEncontradas, Contador).

% Buscar clases ordenadas por numero de propiedades coincidentes
clases_por_coincidencias(ListaPropiedades, ClasesOrdenadas) :-
    obtener_todos_los_generos(TodasClases),
    findall(coincidencias(Contador, Clase), (
        member(Clase, TodasClases),
        contar_propiedades_clase(Clase, ListaPropiedades, Contador),
        Contador > 0
    ), ClasesConCoincidencias),
    sort(1, @>=, ClasesConCoincidencias, ClasesOrdenadas).

% Analisis avanzado: encontrar patrones en propiedades
patron_propiedades(Patron, Clases) :-
    (   Patron = instrumentos_similares(Instrumento) ->
        findall(Clase, hereda_propiedad(Clase, usa(Instrumento)), Clases)
    ;   Patron = mismo_origen(Pais) ->
        findall(Clase, hereda_propiedad(Clase, origen_pais(Pais)), Clases)
    ;   Patron = misma_decada(Decada) ->
        findall(Clase, hereda_propiedad(Clase, origen_decada(Decada)), Clases)
    ;   Patron = energia_similar(Nivel) ->
        findall(Clase, hereda_propiedad(Clase, energia(Nivel)), Clases)
    ;   Patron = tempo_rango(Min, Max) ->
        clases_por_tempo(Min, Max, Clases)
    ).