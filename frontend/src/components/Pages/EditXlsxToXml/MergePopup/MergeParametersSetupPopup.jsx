import React, {useEffect, useState} from "react";
import css from "./MergeParametersSetupPopup.module.css";

const MergeParametersSetupPopup = ({
                                       active,
                                       setActive,
                                       setActiveIndex,
                                       toIndex,
                                       bundle,
                                       outerActions,
                                       setOuterActions
                                   }) => {

    const columnsFromFile = bundle[0];
    const columnsToFile = bundle[1];
    const arrows = bundle[2];
    const zip = bundle[3];
    const [values, setValues] = useState({multigroupSplitter: " ",});
    const [exampleStringSource, setExampleStringSource] = useState([]);
    const [exampleStringResult, setExampleStringResult] = useState("");
    // const [aboba, setAboba] = useState("aboba");

    const [formColumns, setFormColumns] = useState([]);
    const [chosenGroup, setDisabledGroup] = useState(1);

    const [resultLength, setResultLength] = useState(-1);

    const [castTargetType, setCastTargetType] = useState("String");

    useEffect(() => {
        initPopupValues();
        updateResult();
        generateMergePatternString();
        console.log('zip', zip);
    }, [active]);

    useEffect(() => {
        generateMergePatternString();
    }, [values, castTargetType]);

    const generateMergePatternString = () => {
        let result;

        if (resultLength === -1)
            return;

        if (arrows.length === 0)
            return;

        if (values.multigroupSplitter !== '') {
            result = [];
            let i = -1;
            for (let key in values) {
                result.push('${' + i + '}');
                result.push(values.multigroupSplitter);
                i++;
            }
            result = result.slice(2, result.length - 1).join('');

        } else {
            result = [];
            let i = -1;
            for (let key in values) {
                result.push('${' + i + '}');
                result.push(values[key]);
                i++;
            }
            result = result.slice(2, result.length - 1).join('');
        }

        console.log('result');
        console.log(result);

        let index = columnsFromFile.map(element => element[0][0]);
        let endIndex = columnsToFile.map(element => element[0][0]);
        let sources = [];
        for (let key in values) {
            sources.push(index.indexOf(key));
        }
        sources = sources.slice(1, sources.length);

        let resultNode = {
            "type": "merge",
            "initialColumns": sources.map(ind => [index[ind]]),
            "targetColumn": [endIndex[toIndex]],
            "pattern": result
        };

        // let castNode = {
        //     "type": "cast",
        //     "targetColumn": [endIndex[toIndex]],
        //     "targetType": castTargetType
        // }

        let outerActns = outerActions;
        outerActns['to-' + toIndex] = resultNode;
        // outerActns['to-' + toIndex + '-cast'] = castNode;
        setOuterActions(outerActns);

        console.log('outerActnsMerge');
        console.log(outerActns);

        console.log(resultNode);
    };

    const initPopupValues = () => {
        console.log('INIT POPUP VALUES');
        let mergeTarget = columnsToFile[toIndex];
        let mappedArrows = arrows.map(node => [Number(node[0].replace('from-', '')), Number(node[1].replace('to-', ''))]);
        let filteredArrows = mappedArrows.filter(node => node[1] === toIndex);
        let formCols = filteredArrows.map(arrow => columnsFromFile[arrow[0]][0]);

        console.log('columnsFromFile');
        console.log(columnsFromFile);
        console.log('formCols');
        console.log(formCols);
        console.log('filteredArrows');
        console.log(filteredArrows);

        let vals = structuredClone(values);
        formCols.forEach(value => vals[value] = vals[value] || "");


        let exampleStringSource = formCols.map((value, index) => zip[value]);
        let result = exampleStringSource.join(values.multigroupSplitter);

        console.log('setValues ->', vals);
        console.log('setResultLength -> ', formCols.length);
        console.log('setFormColumns -> ', formCols.slice(0, formCols.length - 1));
        console.log('setExampleStringSource -> ', [...exampleStringSource]);
        console.log('setExampleStringResult -> ', result);

        setValues(structuredClone(vals));
        setResultLength(formCols.length);
        setFormColumns(formCols.slice(0, formCols.length - 1));
        setExampleStringSource([...exampleStringSource]);
        setExampleStringResult(result);
        // setAboba(result);

        logState('init popup values');

        // console.log('============================================');
        // console.log('mappedArrows', mappedArrows);
        // console.log('filteredArrows', filteredArrows);
        // console.log('columnsFromFile', columnsFromFile);
        // console.log('formCols', formCols);
        // console.log('formColumns', formColumns);
        // console.log('values', values);
        // console.log('exampleStringSource', exampleStringSource);
        // console.log('exampleStringResult', exampleStringResult);
        // console.log('============================================');

        // console.log('start', columnsToFile);
        // console.log('exampleStringSource', exampleStringElements);
        // console.log('toIndex', toIndex);
        // console.log('columnsFromFile', columnsFromFile[toIndex][0]);
        // console.log('mappedArrows', mappedArrows);
        // console.log('filteredArrows', filteredArrows);
        // console.log('formColumns', formCols);
        // console.log('values', values);
    };

    const logState = (from) => {
        console.log('============= LOGSTATE from:' + from + '========================');
        console.log('values', values);
        console.log('resultLength', resultLength);
        console.log('formColumns', formColumns);
        console.log('exampleStringSource', exampleStringSource);
        console.log('exampleStringResult', exampleStringResult);
        console.log('============================================');
    };

    function updateResult() {
        let isSecondGroupDisabled = false;
        for (let i = 0; i < formColumns.length; i++) {
            isSecondGroupDisabled = isSecondGroupDisabled || (values[formColumns[i] || 'multigroupSplitter'] !== '');
        }

        if (values.multigroupSplitter === '') {
            // Доступны все поля
            setDisabledGroup(2);
            setExampleStringResult([...exampleStringSource].join(''));
            // setAboba([...exampleStringSource].join(''));
        } else if (values.multigroupSplitter !== '' && exampleStringSource.length !== 0) {
            // Доступно только верхнее поле
            setDisabledGroup(1);
            // console.log('Возможное место ошибки');
            // console.log([...exampleStringSource].join(values.multigroupSplitter));
            // setExampleStringResult([...exampleStringSource].join(values.multigroupSplitter));
            // console.log('KAIF');
            // console.log('exampleStringSource');
            // console.log(exampleStringSource);
            // console.log('exampleStringResult');
            // console.log(exampleStringResult);
            setExampleStringResult([...exampleStringSource].join(values.multigroupSplitter));
            // setAboba([...exampleStringSource].join(values.multigroupSplitter));
        }

        if (isSecondGroupDisabled) {
            // Доступно только нижнее поле
            setDisabledGroup(0);

            let result = "";
            let sourceString = [...exampleStringSource];
            for (let i = 0; i < sourceString.length; i++) {
                result += sourceString[i] + (values[formColumns[i]] ? values[formColumns[i]] : "");
            }
            setExampleStringResult(result);
            // setAboba(result);
        }
    }

    const handleChange = (event) => {
        // console.log(event.target.previousSibling);
        let id = event.target.id;
        // console.log(event.target.id);
        let inputtedValue = event.target.value;
        console.log(id);
        console.log(inputtedValue);
        values[formColumns[id] || 'multigroupSplitter'] = inputtedValue;
        setValues(structuredClone(values));
        console.log(values);

        updateResult();
    };

    const applyMergeParameters = (event) => {
        setActive(!active);
        setActiveIndex(-1);
    };

    const cancelMergePopup = (event) => {
        setActive(!active);
        setActiveIndex(-1);
    };

    const decorateOutput = (input) => {
        return input.map(token => "«" + token + "»").join(", ");
    };

    const onSelectChange = (value) => {
        console.log(value.target.value);
        setCastTargetType(value.target.value);
    };

    return (
        <div className={active ? css.popup : css.hide}
             onClick={e => {
                 setActive(!active);
                 setActiveIndex(-1);
             }}
             onKeyDown={e => {
                 console.log(e.key);
                 if (e.key === 'Escape') {
                     setActive(!active);
                     setActiveIndex(-1);
                 }
             }}
             tabIndex={0}
        >
            <div className={css.content} onClick={e => e.stopPropagation()}>
                <h1 className={css.header_top}>Выберите символы-соединители</h1>

                {/*<hr className={css.horizontal_ruler}/>*/}

                <div className={css.splitters_group}>
                    <h2 className={css.header_middle}>Символ на всю группу</h2>
                    <div className={css.data_input_block}>
                        <p className={css.data_input_element}>Символ на всю группу</p>
                        <span
                            style={{whiteSpace: 'pre'}}>{`«${values.multigroupSplitter === " " ? "Пробел" : values.multigroupSplitter}»`}</span>
                        <input id="all" className={css.data_input_field} type="text" onChange={handleChange}
                               defaultValue={values.multigroupSplitter}
                               disabled={chosenGroup === 0}></input>
                    </div>
                </div>

                {/*<hr className={css.horizontal_ruler}/>*/}

                <div className={css.splitters_group}>
                    <h2 className={css.header_middle}>Символ каждой группы</h2>
                    {formColumns.map((col, index) =>
                        <div className={css.data_input_block}>
                            <p className={css.data_input_element}>Символ после {formColumns[index]}</p>
                            <span
                                style={{whiteSpace: 'pre'}}>{`«${values[col] === " " ? "Пробел" : values[col]}»`}</span>
                            <input id={`${index}`} className={css.data_input_field} type="text"
                                   onChange={handleChange}
                                   defaultValue={values[col]}
                                   disabled={chosenGroup === 1}></input>
                        </div>
                    )}

                </div>

                <hr className={css.horizontal_ruler}/>

                <div className={css.example}>
                    <p>Строка:</p>
                    <p>{`${decorateOutput(exampleStringSource)}`}</p>
                </div>
                <div className={css.example}>
                    <p>Будет соединена в:</p>
                    <p>{`«${exampleStringResult}»`}</p>
                    {/*<p>{`«${aboba}»`}</p>*/}
                </div>
                {/*<div className={css.example}>*/}
                {/*    <p>И будет иметь тип данных:</p>*/}
                {/*    <select className={css.select} onChange={onSelectChange}>*/}
                {/*        <option value="String">Строка</option>*/}
                {/*        <option value="Boolean">Булево значение</option>*/}
                {/*        <option value="Number">Число</option>*/}
                {/*        <option value="Null">Null</option>*/}
                {/*        <option value="Object">Объект</option>*/}
                {/*    </select>*/}
                {/*</div>*/}

                {/*<button id="getResult" onClick={initPopupValues}>Подтвердить</button>*/}

                <div className={css.button_block}>
                    <button id="getResult" onClick={applyMergeParameters} className={css.apply_button}>Подтвердить
                    </button>
                    <button id="getResult" onClick={cancelMergePopup} className={css.cancel_button}>Отмена</button>
                </div>
            </div>
        </div>
    );
};

export default MergeParametersSetupPopup;