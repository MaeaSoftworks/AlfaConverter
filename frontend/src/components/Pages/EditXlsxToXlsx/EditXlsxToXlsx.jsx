import css from './EditXlsxToXlsx.module.css';
import upload_image from './upload_header_analytics_img.png';
import connection_light from './connection_light_bg.png';
import split_light from './split_light_bg.png';
import merge_light from './merge_light_bg.png';
import delete_light from './delete.png';
import {Link, useLocation, useNavigate} from "react-router-dom";
import Xarrow, {Xwrapper} from "react-xarrows";
import React, {useEffect, useId, useState} from "react";
import SplitParametersSetupPopup from "./SplitPopup/SplitParametersSetupPopup";
import MergeParametersSetupPopup from "./MergePopup/MergeParametersSetupPopup";

const EditXlsxToXlsx = () => {

    const {state} = useLocation();
    const columns = state.columns;
    const columnsFromFile = columns[0];
    const columnsToFile = columns[1];
    const fileFrom = state.fileFrom;
    const fileTo = state.fileTo;
    const zipped = state.zipped;

    const [shouldRedirect, setShouldRedirect] = useState(false);
    const [requestBody, setRequestBody] = useState("");

    const [isResponseOk, setIsResponseOk] = useState(true);

    const navigate = useNavigate();

    React.useEffect(() => {
        if (shouldRedirect) {
            navigate('/result', {state: {requestBody: requestBody, fileFrom: fileFrom, fileTo: fileTo}});
        }
    });

    const action = {
        default: {
            type: 'default',
            arrowColor: null
        },
        connect: {
            type: 'connect',
            arrowColor: 'gray'
        },
        split: {
            type: 'split',
            arrowColor: '#FFB580'
        },
        merge: {
            type: 'merge',
            arrowColor: '#809CFF'
        },
        delete: {
            type: 'delete',
            arrowColor: null
        }
    };
    const status = {
        default: 'default',
        pending: 'pending'
    };

    const actionObj = {
        actions: []
    };

    const [outerActions, setOuterActions] = useState({});

    const connections = {};

    const [currentAction, setCurrentAction] = useState(action.default);
    const [currentStatus, setCurrentStatus] = useState(status.default);
    const [pendingBlockId, setPendingBlockId] = useState('');
    const [clickedButton, setClickedButton] = useState('');
    const [arrows, setArrows] = useState([]);
    const [activeSplitIndex, setActiveSplitIndex] = useState(-1);
    const [activeMergeIndex, setActiveMergeIndex] = useState(-1);


    const [arrowMoved, setArrowMoved] = useState(false);

    const structBlockOutArrowHandler = (event) => {
        let target = event.target;
        let targetId = target.id;

        console.log('id: ', targetId);
        console.log(target.children);
        console.log(currentStatus);

        if (currentAction.type === 'delete')
            return;

        if (currentAction.type !== action.default.type) {
            if (currentStatus === status.default) {
                setPendingBlockId(target.id);
                setCurrentStatus(status.pending);
                // let arrow = <Xarrow key={startId+endId} start={startId} end={endId}/>;
                // if (currentAction.type === 'split') {
                //     event.target.children[1].visibility = 'visible';
                //     console.log('splitus');
                // }
                arrows.push([target.id, 'dynamic_arrow_endpoint', currentAction, 'temporary_arrow']);
                setArrows(arrows);
            }
            console.log('target');
            console.log(target);
            console.log('arrows');
            console.log(arrows);
        }
    };

    const structBlockInArrowHandler = (event) => {
        let target = event.target;
        let targetId = target.id;

        console.log('id: ', targetId);
        console.log(target.children);
        console.log(currentStatus);

        if (isToBlockOccupiedWithMerge(targetId) && currentAction.type !== 'merge')
            return;

        if (currentAction.type === 'delete')
            return;

        if (currentAction.type !== action.default.type) {
            if (currentStatus === status.pending) {
                let startId = pendingBlockId;
                let endId = target.id;
                // let arrow = <Xarrow key={startId+endId} start={startId} end={endId}/>;
                arrows.pop();
                arrows.push([startId, endId, currentAction, startId + endId]);
                setArrows(arrows);
                setPendingBlockId('');
                setCurrentStatus(status.default);
            }
            console.log(arrows);
        }
    };

    const isToBlockOccupiedNotWithMerge = (blockId) => {
        return arrows.filter(arrow => arrow[1] === blockId && arrow[2].type !== 'merge').length !== 0;
    };

    const isToBlockOccupiedWithMerge = (blockId) => {
        return arrows.filter(arrow => arrow[1] === blockId && arrow[2].type === 'merge').length !== 0;
    };

    const structBlockArrowClickHandler = (event) => {
        console.log(event.target.id);
        console.log(arrows);
        console.log(event);
        if (currentAction.type === action.delete.type) {
            setArrows(arrows.filter(arrow => !arrow.includes(event.target.id)));
        }
    };

    const splitPopupClickHandler = (event) => {
        breakCurrentUnattachedArrow();
        setActiveSplitIndex(Number(event.target.dataset.targetIndex));
        setActive(true);
    };

    const mergePopupClickHandler = (event) => {
        breakCurrentUnattachedArrow();
        setActiveMergeIndex(Number(event.target.dataset.targetIndex));
        setActive(true);
    };

    const datatypePopupClickHandler = (event) => {
        setActive(true);
    };

    const connectOnClick = (event) => {
        event.preventDefault();
        switchActions(action.connect);
    };
    const splitOnClick = (event) => {
        event.preventDefault();
        switchActions(action.split);
    };
    const mergeOnClick = (event) => {
        event.preventDefault();
        switchActions(action.merge);
    };
    const deleteOnClick = (event) => {
        event.preventDefault();
        switchActions(action.delete);
    };
    const resetOnClick = (event) => {
        event.preventDefault();
        setArrows([]);
    };

    const switchActions = (buttonAction) => {
        console.log(currentAction.type);
        if (currentAction.type === buttonAction.type) {
            console.log(action.default.type);
            setCurrentAction(action.default);
            setClickedButton(action.default.type)
        } else {
            console.log(buttonAction.type);
            setCurrentAction(buttonAction);
            setClickedButton(buttonAction.type)
        }
    };

    const mouseMoveHandler = (event) => {
        const endpoint = document.getElementById('dynamic_arrow_endpoint');
        let x = event.pageX;
        let y = event.pageY;
        endpoint.style.left = x + 'px';
        endpoint.style.top = y + 'px';
        setArrowMoved(!arrowMoved);
    };

    const updateState = () => {
        setArrowMoved(!arrowMoved);
    };

    const apply = (event) => {
        let i = 0;
        console.log('columnsFromFile');
        console.log(columnsFromFile);
        console.log('arrows');
        console.log(arrows);

        let binds = {};
        let splits = {};
        let merges = {};

        arrows.forEach(arrow => {
            let arrowStartId = arrow[0];
            let arrowEndId = arrow[1];

            let arrowStartIndex = findIndexOfXByIdOfY(columnsFromFile, arrow[0]);
            let arrowEndIndex = findIndexOfXByIdOfY(columnsToFile, arrow[1]);

            if (arrow[2].type === 'connect') {
                if (!(arrowStartIndex in binds)) {
                    binds[arrowStartIndex] = [];
                }
                binds[arrowStartIndex].push(findIndexOfXByIdOfY(columnsToFile, arrowEndId)[0]);
            } else if (arrow[2].type === 'split') {
                if (!(arrowStartIndex in splits)) {
                    splits[arrowStartIndex] = [];
                }
                splits[arrowStartIndex].push(findIndexOfXByIdOfY(columnsToFile, arrowEndId)[0]);
            } else {
                if (!(arrowEndIndex in merges)) {
                    merges[arrowEndIndex] = [];
                }
                merges[arrowEndIndex].push(findIndexOfXByIdOfY(columnsFromFile, arrowStartId)[0]);
            }
        });

        console.log(binds);
        console.log(splits);
        console.log(merges);

        Object.keys(binds).forEach(bind => {
            let res = {
                "type": "bind",
                "initialColumn": columnsFromFile[bind][0],
                "targetColumn": [columnsToFile[binds[bind][0]]][0][0]
            };
            console.log(res);
            actionObj.actions.push(res);
        });

        Object.keys(splits).forEach(split => {
            let res = {
                "type": "split",
                "initialColumn": columnsFromFile[split][0],
                "targetColumns": splits[split].map(index => columnsToFile[index][0]),
                // "pattern": Array(splits[split].length).fill("(\\S+)").join(" ")
            };

            if (('from-' + split) in outerActions) {
                console.log('aboba');
                res['pattern'] = outerActions['from-' + split].pattern;
            } else {
                res['pattern'] = Array(splits[split].length).fill("(\\S+)").join(" ");
            }

            console.log(res);
            actionObj.actions.push(res);
        });

        Object.keys(merges).forEach(merge => {
            let res = {
                "type": "merge",
                "initialColumns": merges[merge].map(index => columnsFromFile[index][0]),
                "targetColumn": columnsFromFile[merge][0],
                // "pattern": merges[merge].map(number => "$" + number).join(' ')
            };

            if (('to-' + merge) in outerActions) {
                res['pattern'] = outerActions['to-' + merge].pattern;
            } else {
                res['pattern'] = merges[merge].map((number, index) => "${" + index + "}").join(' ');
            }

            if (('to-' + merge + '-cast') in outerActions) {
                actionObj.actions.push(outerActions['to-' + merge + '-cast']);
            }

            console.log(res);
            actionObj.actions.push(res);
        });

        console.log(JSON.stringify(actionObj));
        // setRequestBody(JSON.stringify(actionObj));
        // setShouldRedirect(true);

        downloadResult(JSON.stringify(actionObj));
    };

    const downloadResult = (conversion) => {
        var formdata = new FormData();
        formdata.append("conversion", conversion);
        formdata.append("source", fileFrom, fileFrom.name);
        formdata.append("modifier", fileTo, fileTo.name);

        var requestOptions = {
            method: 'POST',
            body: formdata,
            redirect: 'follow'
        };

        let status = 200;

        fetch("http://127.0.0.1:8080/api/xlsx/convert", requestOptions)
            .then(response => {
                console.log(response);
                status = response.status;
                return response.blob();
            })
            .then(result => {
                if (status === 200) {
                    setIsResponseOk(true);
                    console.log(result);
                    const url = window.URL.createObjectURL(new Blob([result]));
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', `${Date.now()}.xlsx`);
                    document.body.appendChild(link);
                    link.click();
                } else if (status === 500) {
                    setIsResponseOk(false);
                }
            })
            .catch(error => console.log('error', error));
    };

    const breakCurrentUnattachedArrow = () => {
        if (arrows.length === 0)
            return;
        let lastArrow = arrows[arrows.length - 1];
        if (lastArrow[1] === 'dynamic_arrow_endpoint' && lastArrow[3] === 'temporary_arrow') {
            setPendingBlockId('');
            setCurrentStatus(status.default);
            arrows.pop();
        }
    };

    const findIndexOfXByIdOfY = (columnsArr, Id) => {
        let colIndex = 0;
        let result = [];
        console.log('Передано!');
        console.log(columnsArr, Id);
        columnsArr.forEach(column => {
            if (column[1] === Id) {
                result.push(colIndex);
            }
            colIndex++;
        });
        return result;
    };

    const findConnectTargetIndexByTargetId = (Id) => {
        let colIndex = 0;
        let result = -1;
        columnsToFile.forEach(column => {
            if (column[1] === Id) {
                result = colIndex;
            }
            colIndex++;
        });
        return result;
    };

    const findSplitTargetsBySourceId = (Id) => {
        let result = [];
        arrows.forEach(arrow => {
            if (arrow[0] === Id) {
                result.push(arrow);
            }
        });
        return result;
    };

    const findSplitTargetsIndexByTargetId = (Id) => {
        let colIndex = 0;
        let result = [];
        columnsToFile.forEach(column => {
            if (column[1] === Id) {
                console.log('Найдено:', column);
                result.push(colIndex);
            }
            colIndex++;
        });
        return result;
    };

    const isThereNoLinkedArrowsOfTypeWithId = (typeOfArrow, IdOfArrow) => {
        return arrows.filter(arrow => arrow[2].type === typeOfArrow && arrow.includes(IdOfArrow) && arrow[1] !== 'dynamic_arrow_endpoint').length === 0;
    };

    const isThereNoPendingArrowsOfTypeWithId = (typeOfArrow, IdOfArrow) => {
        return arrows.filter(arrow => arrow[2].type === typeOfArrow && arrow.includes(IdOfArrow)).length === 0;
    };

    const [active, setActive] = useState(false);

    return (
        <div className={css.page} onKeyDown={e => {
            if (e.key === 'Escape') {
                setActive(!active);
                setActiveSplitIndex(-1);
                setActiveMergeIndex(-1);
            }
        }}>
            <div className={css.header_container}>
                <div className={css.header}>
                    <div className={css.header_text_block}>
                        <h1 className={css.header_header}>Изменение структуры</h1>
                        <p className={css.header_description}>Вам предстоит выбрать как будет выглядеть Ваш документ
                            после
                            конвертации</p>
                    </div>
                    <img src={upload_image} className={css.header_img}/>
                </div>
                <div className={css.block}/>
            </div>
            <div className={css.edit}>
                {/*<Popup active={active} dataBundle={popupDataBundle}/>*/}
                <div className={css.scheme_board} onMouseMove={mouseMoveHandler}
                     onMouseLeave={breakCurrentUnattachedArrow} onScroll={updateState}>
                    <Xwrapper>

                        {columnsFromFile.map((columnName, index) =>
                            <SplitParametersSetupPopup active={activeSplitIndex === index}
                                                       setActive={setActive}
                                                       setActiveIndex={setActiveSplitIndex}
                                                       fromIndex={index}
                                                       bundle={[columnsFromFile, columnsToFile, arrows, zipped]}
                                                       outerActions={outerActions}
                                                       setOuterActions={setOuterActions}/>
                        )}

                        {columnsFromFile.map((columnName, index) =>
                            <MergeParametersSetupPopup active={activeMergeIndex === index}
                                                       setActive={setActive}
                                                       setActiveIndex={setActiveMergeIndex}
                                                       toIndex={index}
                                                       bundle={[columnsFromFile, columnsToFile, arrows, zipped]}
                                                       outerActions={outerActions}
                                                       setOuterActions={setOuterActions}/>
                        )}


                        <div className={css.dynamic_arrow_endpoint} id='dynamic_arrow_endpoint'>
                        </div>
                        <div className={css.file_struct}>
                            <h1 className={css.struct_header}>Исходная структура</h1>
                            {/*<div className={css.struct_block} id={'init-' + useId()} onClick={structBlockOutArrowHandler}>*/}
                            {/*    <input className={css.struct_input} type='text' value='ФИО'/>*/}
                            {/*</div>*/}
                            {columnsFromFile.map((columnName, index) =>
                                <div className={css.struct_block_container}>
                                    <div className={css.struct_block} id={columnName[1]} data-index={index}
                                         onClick={structBlockOutArrowHandler}>
                                        {/*<Popup active={active} setActive={activesSplits[index]} initString = {columnsFromFile[0]} targets = {3} />*/}
                                        <p className={css.struct_input}>{columnName[0]}</p>
                                    </div>
                                    <div className={
                                        `${css.from_point} 
                                        ${isThereNoPendingArrowsOfTypeWithId('split', columnName[1]) ? '' : css.split_point}
                                        ${isThereNoPendingArrowsOfTypeWithId('merge', columnName[1]) ? '' : css.merge_point}
                                        ${isThereNoPendingArrowsOfTypeWithId('connect', columnName[1]) ? '' : css.connect_point}`
                                    }/>
                                    {/*<SplitParametersSetupPopup active={activeSplitIndex === index}*/}
                                    {/*                           setActive={setActive}*/}
                                    {/*                           setActiveIndex={setActiveSplitIndex}*/}
                                    {/*                           fromIndex={index}*/}
                                    {/*                           bundle={[columnsFromFile, columnsToFile, arrows, zipped]}*/}
                                    {/*                           outerActions={outerActions}*/}
                                    {/*                           setOuterActions={setOuterActions}/>*/}
                                    <button onClick={splitPopupClickHandler}
                                            data-target-index={index}
                                            className={css.popup_trigger + ' ' + css.split_popup_trigger + ' ' + (isThereNoPendingArrowsOfTypeWithId('split', columnName[1]) ? css.popup_trigger_invisible : '')}></button>
                                </div>
                            )}
                        </div>
                        <div className={css.file_struct}>
                            <h1 className={css.struct_header}>Необходимая структура</h1>
                            {/*<div className={css.struct_block} id={'wanted-' + useId()}*/}
                            {/*     onClick={structBlockInArrowHandler}>*/}
                            {/*    <input className={css.struct_input} type='text' value='Фамилия'/>*/}
                            {/*</div>*/}
                            {columnsToFile.map((columnName, index) =>
                                <div className={css.struct_block_container}>
                                    <div className={css.struct_block} id={columnName[1]} data-index={index}
                                         onClick={structBlockInArrowHandler}>
                                        <p className={css.struct_input}>{columnName[0]}</p>
                                    </div>
                                    <div className={
                                        `${css.to_point} 
                                        ${isThereNoPendingArrowsOfTypeWithId('split', columnName[1]) ? '' : css.split_point}
                                        ${isThereNoPendingArrowsOfTypeWithId('merge', columnName[1]) ? '' : css.merge_point}
                                        ${isThereNoPendingArrowsOfTypeWithId('connect', columnName[1]) ? '' : css.connect_point}`
                                    }/>
                                    {/*<MergeParametersSetupPopup active={activeMergeIndex === index}*/}
                                    {/*                           setActive={setActive}*/}
                                    {/*                           setActiveIndex={setActiveMergeIndex}*/}
                                    {/*                           toIndex={index}*/}
                                    {/*                           bundle={[columnsFromFile, columnsToFile, arrows, zipped]}*/}
                                    {/*                           outerActions={outerActions}*/}
                                    {/*                           setOuterActions={setOuterActions}/>*/}
                                    <button onClick={mergePopupClickHandler}
                                            data-target-index={index}
                                            className={css.popup_trigger + ' ' + css.merge_popup_trigger + ' ' + (isThereNoPendingArrowsOfTypeWithId('merge', columnName[1]) ? css.popup_trigger_invisible : '')}></button>
                                </div>
                            )}
                        </div>
                        {
                            arrows.map(startAndEnd => <Xarrow key={startAndEnd[3]} start={startAndEnd[0]}
                                                              id={startAndEnd[3]}
                                                              end={startAndEnd[1]}
                                                              color={startAndEnd[2].arrowColor}
                                                              strokeWidth={4}
                                                              headSize={4}
                                                              passProps={
                                                                  {
                                                                      onClick: structBlockArrowClickHandler,
                                                                      id: (startAndEnd[3]),
                                                                      cursor: "pointer",
                                                                      operationtype: startAndEnd[2].type
                                                                  }
                                                              }/>)}
                    </Xwrapper>
                </div>
                <div className={css.control_panel}>
                    <h1 className={css.control_panel_header}>Инструменты</h1>
                    <ul className={css.controls}>
                        <li className={css.control}>
                            <button
                                className={clickedButton === 'connect' ? css.control_button_connection_active : css.control_button_connection}
                                onClick={connectOnClick}/>
                            <div>
                                <h2 className={css.control_name}>Связь между столбцами</h2>
                                <p className={css.control_description}>Используйте для связи столбцов, к которым хотите
                                    применить изменение</p>
                            </div>
                        </li>
                        <li className={css.control}>
                            <button
                                className={clickedButton === 'split' ? css.control_button_split_active : css.control_button_split}
                                onClick={splitOnClick}/>
                            <div>
                                <h2 className={css.control_name}>Разделение</h2>
                                <p className={css.control_description}>Выберите стрелку, к которой должно быть применено
                                    данное действие</p>
                            </div>
                        </li>
                        <li className={css.control}>
                            <button
                                className={clickedButton === 'merge' ? css.control_button_merge_active : css.control_button_merge}
                                onClick={mergeOnClick}/>
                            <div>
                                <h2 className={css.control_name}>Слияние</h2>
                                <p className={css.control_description}>Выберите стрелку, к которой должно быть применено
                                    данное действие</p>
                            </div>
                        </li>
                        <li className={css.control}>
                            <button
                                className={clickedButton === 'delete' ? css.control_button_delete_active : css.control_button_delete}
                                onClick={deleteOnClick}/>
                            <div>
                                <h2 className={css.control_name}>Удалить</h2>
                                <p className={css.control_description}>Выберите действие/связь необходимое к
                                    удалению</p>
                            </div>
                        </li>
                        <li className={css.control}>
                            <button
                                className={css.control_button_reset}
                                onClick={resetOnClick}/>
                            <div>
                                <h2 className={css.control_name}>Сбросить</h2>
                                <p className={css.control_description}>Сбросить все выстроенные связи</p>
                            </div>
                        </li>
                        <li>
                            {/*<Link to='/result'>*/}
                            <button className={css.apply_button} onClick={apply}>Преобразовать</button>
                            <p className={isResponseOk ? css.server_error_hidden : css.server_error_showed}>Ошибка во
                                время
                                обработки файла на сервере. Возможно, вы указали невозможное преобразование</p>
                            {/*</Link>*/}
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default EditXlsxToXlsx;