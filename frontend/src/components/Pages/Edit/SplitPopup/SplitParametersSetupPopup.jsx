import React, {useState, useEffect} from "react";
import css from "./SplitParametersSetupPopup.module.css";

const SplitParametersSetupPopup = ({active, setActive, setActiveIndex, fromIndex, bundle, outerActions, setOuterActions}) => {

    const columnsFromFile = bundle[0];
    const columnsToFile = bundle[1];
    const arrows = bundle[2];
    const zip = bundle[3];
    const [values, setValues] = useState({multigroupSplitter: " ",});
    const [splitPattern, setSplitPattern] = useState('');
    const [exampleStringSource, setExampleStringSource] = useState("");
    const [exampleStringResult, setExampleStringResult] = useState([]);

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
        generateSplitPatternString();
        console.log('zip', zip);
    }, [active]);

    useEffect(() => {
        generateSplitPatternString();
    }, [values]);

    const generateSplitPatternString = () => {
        let resultSource = "(\\S+)"
        let result;

        if (resultLength === -1)
            return;

        if (arrows.length === 0)
            return;

        if (values.multigroupSplitter !== '') {
            result = [];
            for(let key in values) {
                result.push(resultSource);
                result.push(values.multigroupSplitter);
            }
            result = result.slice(2, result.length-1).join('');
        } else {
            result = [];
            for(let key in values) {
                result.push(resultSource);
                result.push(values[key]);
            }
            result = result.slice(2, result.length-1).join('');
        }
        console.log(result);

        let index = columnsToFile.map(element => element[0]);
        let sources = [];
        for(let key in values) {
            sources.push(index.indexOf(key));
        }
        sources = sources.slice(1, sources.length);

        let resultNode = {
            "type": "split",
            "initial-column": fromIndex,
            "target-columns": sources,
            "pattern": result
        };

        let outerActns = outerActions;
        outerActns['from-' + fromIndex] = resultNode;
        setOuterActions(outerActns);

        console.log('outerActnsSplit');
        console.log(outerActns);

        console.log(resultNode);
    };

    const initPopupValues = () => {
        let start = columnsFromFile[fromIndex];
        let exampleStringSource = zip[columnsFromFile[fromIndex][0]];
        let mappedArrows = arrows.map(node => [Number(node[0].replace('from-', '')), Number(node[1].replace('to-', ''))]);
        let filteredArrows = mappedArrows.filter(node => node[0] === fromIndex);
        let formCols = columnsToFile.map((value, index) => filteredArrows.flat().includes(index) ? value : undefined)
            .filter(value => value).map(value => value[0]);
        // setFormColumns(formCols);

        formCols.forEach(value => values[value] = values[value] || "");
        setValues(values);
        setResultLength(formCols.length);
        setFormColumns(formCols.slice(0, formCols.length-1));
        setExampleStringSource(exampleStringSource);

        let result = exampleStringSource.split(' ');
        if (result.length < formCols.length) {
            console.log('result.length', result.length);
            console.log('resultLength', resultLength);
            for(let i = result.length; i < formCols.length; i++) {
                result.push('');
            }
        }
        setExampleStringResult(result);

        console.log('start', start);
        console.log('exampleStringSource', exampleStringSource);
        // console.log('fromIndex', fromIndex);
        // console.log('columnsFromFile', columnsFromFile[fromIndex][0]);
        console.log('mappedArrows', mappedArrows);
        console.log('filteredArrows', filteredArrows);
        console.log('formCols', formCols);
    };

    function updateResult() {
        let isSecondGroupDisabled = false;
        for (let i = 0; i < formColumns.length; i++) {
            isSecondGroupDisabled = isSecondGroupDisabled || (values[formColumns[i] || 'multigroupSplitter'] !== '');
        }

        if (values.multigroupSplitter === '') {
            // Доступны все поля
            setDisabledGroup(2);

            let result = [exampleStringSource];
            result = expandResult(result, resultLength);
            setExampleStringResult(result);
        } else if (values.multigroupSplitter !== '') {
            // Доступно только верхнее поле
            setDisabledGroup(1);

            let result = exampleStringSource.split(values.multigroupSplitter);
            result = expandResult(result, resultLength);
            setExampleStringResult(result);
        }

        if (isSecondGroupDisabled) {
            // Доступно только нижнее поле
            setDisabledGroup(0);

            let result = [];
            let sourceString = exampleStringSource;
            console.log(exampleStringSource)
            for (let i = 0; i < formColumns.length; i++) {
                let splitter = values[formColumns[i]];
                let splitted = ''
                let tail = sourceString;

                if (splitter !== '') {
                    splitted = sourceString.split(splitter, 1);
                    tail = sourceString.slice(splitted[0].length + splitter.length);
                } else {
                    splitted = sourceString;
                    tail = '';
                }

                console.log('splitted', splitted);
                console.log('tail', tail);
                result.push(splitted);
                sourceString = tail;
            }

            if (sourceString !== '')
                result.push(sourceString);

            result = expandResult(result, resultLength);
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

    const expandResult = (resultElements, neededLength) => {
        resultElements = [...resultElements];
        for(let i = resultElements.length; i < neededLength; i++) {
            resultElements.push('');
        }
        return resultElements;
    };

    const applySplitParameters = (event) => {
        setActive(!active);
        setActiveIndex(-1);
    };

    const decorateOutput = (input) => {
        return input.map(token => "«" + token + "»").join(", ");
    };

    return (
        <div className={active ? css.popup : css.hide} onClick={e => {
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
                <h1 className={css.header_top}>Выберите символы-разделители</h1>

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
                            <p className={css.data_input_element}>Отделяем {col}</p>
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
                    <p>{`«${exampleStringSource}»`}</p>
                </div>
                <div className={css.example}>
                    <p>Будет разделена на:</p>
                    <p>{`${decorateOutput(exampleStringResult)}`}</p>
                </div>

                <button id="getResult" onClick={applySplitParameters} className={css.apply_button}>Подтвердить</button>

            </div>
        </div>
    );
};

export default SplitParametersSetupPopup;