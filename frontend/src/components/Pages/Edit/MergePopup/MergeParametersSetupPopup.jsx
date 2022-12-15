import React, {useState, useEffect} from "react";
import css from "./MergeParametersSetupPopup.module.css";

const MergeParametersSetupPopup = ({active, setActive, setActiveIndex, toIndex, bundle, outerActions, setOuterActions}) => {

    const columnsFromFile = bundle[0];
    const columnsToFile = bundle[1];
    const arrows = bundle[2];
    const zip = bundle[3];
    const [values, setValues] = useState({multigroupSplitter: " ",});
    const [exampleStringSource, setExampleStringSource] = useState([]);
    const [exampleStringResult, setExampleStringResult] = useState("");

    const [formColumns, setFormColumns] = useState([]);
    const [chosenGroup, setDisabledGroup] = useState(1);

    const [resultLength,setResultLength] = useState(-1);

    const splitWithTail = (str, delim, count) => {
        let parts = str.split(delim);
        let tail = parts.slice(count).join(delim);
        let result = parts.slice(0, count);
        result.push(tail);
        return result;
    };

    useEffect(() => {
        initPopupValues();
        updateResult();
        generateMergePatternString();
        console.log('zip', zip);
    }, [active]);

    useEffect(() => {
        generateMergePatternString();
    }, [values]);

    const generateMergePatternString = () => {
        let resultSource = "$"
        let result;

        if (resultLength === -1)
            return;

        if (arrows.length === 0)
            return;

        if (values.multigroupSplitter !== '') {
            let index = columnsFromFile.map(element => element[0]);
            result = [];
            for(let key in values) {
                result.push('${' + index.indexOf(key) + '}');
                result.push(values.multigroupSplitter);
            }
            result = result.slice(2, result.length-1).join('');

        } else {
            let index = columnsFromFile.map(element => element[0]);
            result = [];
            for(let key in values) {
                result.push('${' + index.indexOf(key) + '}');
                result.push(values[key]);
            }
            result = result.slice(2, result.length-1).join('');
            // console.log(index);
        }
        console.log(result);

        let index = columnsFromFile.map(element => element[0]);
        let sources = [];
        for(let key in values) {
            sources.push(index.indexOf(key));
        }
        sources = sources.slice(1, sources.length);

        let resultNode = {
            "type": "merge",
            "initial-columns": sources,
            "target-column": toIndex,
            "pattern": result
        };

        let outerActns = outerActions;
        outerActns['to-' + toIndex] = resultNode;
        setOuterActions(outerActns);

        console.log('outerActnsMerge');
        console.log(outerActns);

        console.log(resultNode);
    };

    const initPopupValues = () => {
        let mergeTarget = columnsToFile[toIndex];
        let mappedArrows = arrows.map(node => [Number(node[0].replace('from-', '')), Number(node[1].replace('to-', ''))]);
        let filteredArrows = mappedArrows.filter(node => node[1] === toIndex).map(value => value[0]);
        let formCols = columnsFromFile.map((value, index) => filteredArrows.flat().includes(index) ? value : undefined)
            .filter(value => value).map(value => value[0]);
        setFormColumns(formCols);
        formCols.forEach(value => values[value] = values[value] || "");
        setValues(values);
        setResultLength(formCols.length);

        let exampleStringElements = formCols.map((value, index) => zip[value]);
        setFormColumns(formCols.slice(0, formCols.length-1));
        setExampleStringSource(exampleStringElements);
        setExampleStringResult(exampleStringElements.join(values.multigroupSplitter));

        console.log('============================================');
        console.log('mergeTartget', mergeTarget);
        console.log('mappedArrows', mappedArrows);
        console.log('filteredArrows', filteredArrows);
        console.log('columnsFromFile', columnsFromFile);
        console.log('formCols', formCols);
        console.log('formColumns', formColumns);
        console.log('exampleStringElements', exampleStringElements);
        console.log('values', values);
        console.log('============================================');

        // console.log('start', columnsToFile);
        // console.log('exampleStringSource', exampleStringElements);
        // console.log('toIndex', toIndex);
        // console.log('columnsFromFile', columnsFromFile[toIndex][0]);
        // console.log('mappedArrows', mappedArrows);
        // console.log('filteredArrows', filteredArrows);
        // console.log('formColumns', formCols);
        // console.log('values', values);
    };

    function updateResult() {
        let isSecondGroupDisabled = false;
        for (let i = 0; i < formColumns.length; i++) {
            isSecondGroupDisabled = isSecondGroupDisabled || (values[formColumns[i] || 'multigroupSplitter'] !== '');
        }

        if (values.multigroupSplitter === '') {
            // Доступны все поля
            setDisabledGroup(2);
            setExampleStringResult(exampleStringSource.join(''));
        } else if (values.multigroupSplitter !== '') {
            // Доступно только верхнее поле
            setDisabledGroup(1);
            setExampleStringResult(exampleStringSource.join(values.multigroupSplitter));
        }

        if (isSecondGroupDisabled) {
            // Доступно только нижнее поле
            setDisabledGroup(0);

            let result = "";
            let sourceString = exampleStringSource;
            for (let i = 0; i < sourceString.length; i++) {
                result += sourceString[i] + (values[formColumns[i]] ? values[formColumns[i]] : "");
            }
            setExampleStringResult(result);
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

    const decorateOutput = (input) => {
        return input.map(token => "«" + token + "»").join(", ");
    };

    const onSelectChange = (value) => {
        console.log(toIndex);
        console.log(value.target.value);

        let index = '' + toIndex;

        let result = {
            [index]: {
                "target-type": value.target.value,
                "data-format": 0
            }
        };

        console.log('result');
        console.log(result);
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
                        <span style={{whiteSpace: 'pre'}}>{`«${values.multigroupSplitter === " " ? "Пробел" : values.multigroupSplitter}»`}</span>
                        <input id="all" className={css.data_input_element} type="text" onChange={handleChange}
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
                            <input id={`${index}`} className={css.data_input_element} type="text"
                                   onChange={handleChange}
                                   defaultValue={values[col]}
                                   disabled={chosenGroup === 1}></input>
                        </div>
                    )}

                </div>

                <div className={css.example}>
                    <p>Строка:</p>
                    <p>{`${decorateOutput(exampleStringSource)}`}</p>
                </div>
                <div className={css.example}>
                    <p>Будет соединена в:</p>
                    <p>{`«${exampleStringResult}»`}</p>
                </div>
                <div className={css.example}>
                    <p>И будет иметь тип данных:</p>
                    <select className={css.select} onChange={onSelectChange}>
                        <option value="XString">Строка</option>
                        <option value="XBoolean">Булево значение</option>
                        <option value="XNumber">Число</option>
                        <option value="XNull">Null</option>
                        <option value="XObject">Объект</option>
                    </select>
                </div>

                {/*<button id="getResult" onClick={initPopupValues}>Подтвердить</button>*/}
                <button id="getResult" onClick={applyMergeParameters} className={css.apply_button}>Подтвердить</button>

            </div>
        </div>
    );
};

export default MergeParametersSetupPopup;