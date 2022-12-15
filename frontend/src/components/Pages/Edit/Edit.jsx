import css from './Edit.module.css';
import upload_image from './upload_header_laptop_img.png';
import connection_light from './connection_light_bg.png';
import split_light from './split_light_bg.png';
import merge_light from './merge_light_bg.png';
import delete_light from './delete.png';
import {Link, useLocation, useNavigate} from "react-router-dom";
import Xarrow, {Xwrapper} from "react-xarrows";
import React, {useEffect, useId, useState} from "react";
import SplitParametersSetupPopup from "./SplitPopup/SplitParametersSetupPopup";
import MergeParametersSetupPopup from "./MergePopup/MergeParametersSetupPopup";

const Edit = () => {

    const {state} = useLocation();
    const columns = state.columns;
    const columnsFromFile = columns[0];
    const columnsToFile = columns[1];
    const fileFrom = state.fileFrom;
    const fileTo = state.fileTo;
    const zipped = state.zipped;

    const [shouldRedirect, setShouldRedirect] = useState(false);
    const [requestBody, setRequestBody] = useState("");

    const navigate = useNavigate();

    React.useEffect(() => {
        if (shouldRedirect) {
            navigate('/result', { state: { requestBody: requestBody, fileFrom: fileFrom, fileTo: fileTo} });
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
            arrowColor: 'red'
        },
        merge: {
            type: 'merge',
            arrowColor: 'blue'
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
      actions: [

      ]
    };

    const [outerActions, setOuterActions] = useState({});

    const connections = {

    };

    const [currentAction, setCurrentAction] = useState(action.default);
    const [currentStatus, setCurrentStatus] = useState(status.default);
    const [pendingBlockId, setPendingBlockId] = useState('');
    const [clickedButton, setClickedButton] = useState('');
    const [arrows, setArrows] = useState([]);
    const [activeSplitIndex, setActiveSplitIndex] = useState(-1);
    const [activeMergeIndex, setActiveMergeIndex] = useState(-1);


    const [arrowMoved, setArrowMoved] = useState(false);

    const structBlockOutArrowHandler = (event) => {
        console.log(event.target.id);
        console.log(event.target.children);
        let target = event.target;
        console.log(currentStatus);
        if (currentAction.type !== action.default.type) {
            if (currentStatus === status.default) {
                setPendingBlockId(target.id);
                setCurrentStatus(status.pending);
                // let arrow = <Xarrow key={startId+endId} start={startId} end={endId}/>;
                if(currentAction.type === 'split') {
                    event.target.children[1].visibility='visible';
                    console.log('splitus');
                }
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
        console.log(event.target.id);
        console.log(event.target.children);
        let target = event.target;
        console.log(currentStatus);
        if (currentAction.type !== action.default.type) {
            if (currentStatus === status.pending) {
                let startId = pendingBlockId;
                let endId = target.id;
                // let arrow = <Xarrow key={startId+endId} start={startId} end={endId}/>;
                arrows.pop();
                arrows.push([startId, endId, currentAction, startId+endId]);
                setArrows(arrows);
                setPendingBlockId('');
                setCurrentStatus(status.default);
            }
            console.log(arrows);
        }
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
        setActiveSplitIndex(Number(event.target.parentNode.dataset.index));
        setActive(true);
    };

    const mergePopupClickHandler = (event) => {
        setActiveMergeIndex(Number(event.target.parentNode.dataset.index));
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

    const switchActions = (buttonAction) => {
        console.log(currentAction.type);
        if(currentAction.type === buttonAction.type) {
            console.log(action.default.type);
            setCurrentAction(action.default);
            setClickedButton(action.default.type)
        }
        else{
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

            if(arrow[2].type === 'connect') {
                if (!(arrowStartIndex in binds)) {
                    binds[arrowStartIndex] = [];
                }
                binds[arrowStartIndex].push(findIndexOfXByIdOfY(columnsToFile, arrowEndId)[0]);
            }else if(arrow[2].type === 'split'){
                if (!(arrowStartIndex in splits)) {
                    splits[arrowStartIndex] = [];
                }
                splits[arrowStartIndex].push(findIndexOfXByIdOfY(columnsToFile, arrowEndId)[0]);
            }else{
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
                "initialColumn": Number(bind),
                "targetColumn": binds[bind][0]
            };
            console.log(res);
            actionObj.actions.push(res);
        });

        Object.keys(splits).forEach(split => {
            let res = {
                "type": "split",
                "initialColumn": Number(split),
                "targetColumns": splits[split],
                // "pattern": Array(splits[split].length).fill("(\\S+)").join(" ")
            };

            if (('from-' + split) in outerActions) {
                console.log('aboba');
                res['pattern'] = outerActions['from-' + split].pattern;
            }else {
                res['pattern'] = Array(splits[split].length).fill("(\\S+)").join(" ");
            }

            console.log(res);
            actionObj.actions.push(res);
        });

        Object.keys(merges).forEach(merge => {
            let res = {
                "type": "merge",
                "initialColumns": merges[merge],
                "targetColumn": Number(merge),
                // "pattern": merges[merge].map(number => "$" + number).join(' ')
            };

            if (('to-' + merge) in outerActions) {
                console.log('bebra');
                res['pattern'] = outerActions['to-' + merge].pattern;
            }else {
                res['pattern'] = merges[merge].map(number => "$" + number).join(' ');
            }

            console.log(res);
            actionObj.actions.push(res);
        });

        console.log(JSON.stringify(actionObj));
        // setRequestBody(JSON.stringify(actionObj));
        // setShouldRedirect(true);
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

    const [active, setActive] = useState(false);

    return (
        <div className={css.page} onKeyDown={e => {
            if (e.key === 'Escape') {
                setActive(!active);
                setActiveSplitIndex(-1);
                setActiveMergeIndex(-1);
            }
        }}>
            <div className={css.header}>
                <div className={css.header_text_block}>
                    <h1 className={css.header_header}>Изменение структуры</h1>
                    <p className={css.header_description}>Вам предстоит выбрать как будет выглядеть Ваш документ после
                        конвертации</p>
                </div>
                <img src={upload_image} className={css.header_img}/>
            </div>
            <div className={css.edit}>
                {/*<Popup active={active} dataBundle={popupDataBundle}/>*/}
                <div className={css.scheme_board} onMouseMove={mouseMoveHandler}>
                    <Xwrapper>
                        <div className={css.dynamic_arrow_endpoint} id='dynamic_arrow_endpoint'>
                        </div>
                        <div className={css.file_struct}>
                            <h1 className={css.struct_header}>Исходная структура</h1>
                            {/*<div className={css.struct_block} id={'init-' + useId()} onClick={structBlockOutArrowHandler}>*/}
                            {/*    <input className={css.struct_input} type='text' value='ФИО'/>*/}
                            {/*</div>*/}
                            {columnsFromFile.map((columnName, index) =>
                                <div className={css.struct_block} id={columnName[1]} data-index={index} onClick={structBlockOutArrowHandler}>
                                    {/*<Popup active={active} setActive={activesSplits[index]} initString = {columnsFromFile[0]} targets = {3} />*/}
                                    <SplitParametersSetupPopup active={activeSplitIndex === index}
                                                               setActive={setActive}
                                                               setActiveIndex={setActiveSplitIndex}
                                                               fromIndex={index}
                                                               bundle={[columnsFromFile, columnsToFile, arrows, zipped]}
                                                               outerActions={outerActions}
                                                               setOuterActions={setOuterActions}/>
                                    <p className={css.struct_input}>{columnName[0]}</p>
                                    <button onClick={splitPopupClickHandler}
                                        className={css.popup_trigger + ' ' + css.split_popup_trigger + ' ' + (arrows.filter(arrow => arrow[2].type === 'split' && arrow.includes(columnName[1])).length === 0 ? css.popup_trigger_invisible: '')}></button>
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
                                <div className={css.struct_block} id={columnName[1]} data-index={index} onClick={structBlockInArrowHandler}>
                                    <MergeParametersSetupPopup active={activeMergeIndex === index}
                                                               setActive={setActive}
                                                               setActiveIndex={setActiveMergeIndex}
                                                               toIndex={index}
                                                               bundle={[columnsFromFile, columnsToFile, arrows, zipped]}
                                                               outerActions={outerActions}
                                                               setOuterActions={setOuterActions}/>
                                    <p className={css.struct_input}>{columnName[0]}</p>
                                    <button onClick={mergePopupClickHandler}
                                        className={css.popup_trigger + ' ' + css.merge_popup_trigger + ' ' + ( arrows.filter(arrow => arrow[2].type === 'merge' && arrow.includes(columnName[1])).length === 0 ? css.popup_trigger_invisible: '')}></button>
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
                            { onClick: structBlockArrowClickHandler,
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
                            <button className={clickedButton === 'connect'? css.control_button_connection_active : css.control_button_connection} onClick={connectOnClick}/>
                            <div>
                                <h2 className={css.control_name}>Связь между столбцами</h2>
                                <p className={css.control_description}>Используйте для связи столбцов, к которым хотите
                                    применить изменение</p>
                            </div>
                        </li>
                        <li className={css.control}>
                            <button className={clickedButton === 'split'? css.control_button_split_active : css.control_button_split} onClick={splitOnClick}/>
                            <div>
                                <h2 className={css.control_name}>Разделение</h2>
                                <p className={css.control_description}>Выберите стрелку, к которой должно быть применено
                                    данное действие</p>
                            </div>
                        </li>
                        <li className={css.control}>
                            <button className={clickedButton === 'merge'? css.control_button_merge_active : css.control_button_merge} onClick={mergeOnClick}/>
                             <div>
                                <h2 className={css.control_name}>Слияние</h2>
                                <p className={css.control_description}>Выберите стрелку, к которой должно быть применено
                                    данное действие</p>
                            </div>
                        </li>
                        <li className={css.control}>
                            <button className={clickedButton === 'delete'? css.control_button_delete_active : css.control_button_delete} onClick={deleteOnClick}/>
                            <div>
                                <h2 className={css.control_name}>Удалить</h2>
                                <p className={css.control_description}>Выберите действие/связь необходимое к
                                    удалению</p>
                            </div>
                        </li>
                        <li>
                            {/*<Link to='/result'>*/}
                                <button className={css.apply_button} onClick={apply}>Преобразовать</button>
                            {/*</Link>*/}
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default Edit;