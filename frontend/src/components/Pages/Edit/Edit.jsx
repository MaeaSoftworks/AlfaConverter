import css from './Edit.module.css';
import upload_image from './upload_image.png';
import connection_light from './connection_light_bg.png';
import split_light from './split_light_bg.png';
import merge_light from './merge_light_bg.png';
import delete_light from './delete.png';
import {Link, useLocation, useNavigate} from "react-router-dom";
import Xarrow, {Xwrapper} from "react-xarrows";
import React, {useEffect, useId, useState} from "react";

const Edit = () => {

    const {state} = useLocation();
    const columns = state.columns;
    const columnsFromFile = state.columns[0];
    const columnsToFile = state.columns[1];
    const fileFrom = state.fileFrom;
    const fileTo = state.fileTo;

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

    const connections = {

    };

    const [currentAction, setCurrentAction] = useState(action.default);
    const [currentStatus, setCurrentStatus] = useState(status.default);
    const [pendingBlockId, setPendingBlockId] = useState('');
    const [clickedButton, setClickedButton] = useState('');

    const [arrows, setArrows] = useState([]);

    const [arrowMoved, setArrowMoved] = useState(false);

    const structBlockOutArrowHandler = (event) => {
        console.log(event.target.id);
        let target = event.target;
        console.log(currentStatus);
        if (currentAction.type !== action.default.type) {
            if (currentStatus === status.default) {
                setPendingBlockId(target.id);
                setCurrentStatus(status.pending);
                // let arrow = <Xarrow key={startId+endId} start={startId} end={endId}/>;
                arrows.push([target.id, 'dynamic_arrow_endpoint', currentAction, 'temporary_arrow']);
                setArrows(arrows);
            }
            console.log(arrows);
        }
    };

    const structBlockInArrowHandler = (event) => {
        console.log(event.target.id);
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

        // arrows.forEach(arrow => {
        //     let arrowStartId = arrow[0];
        //     let arrowEndId = arrow[1];
        //
        //     if(arrow[2].type === 'connect') {
        //         if (!(arrowStartId in binds)) {
        //             binds[arrowStartId] = [];
        //         }
        //         binds[arrowStartId].push(arrowEndId);
        //     }else if(arrow[2].type === 'split'){
        //         if (!(arrowStartId in splits)) {
        //             splits[arrowStartId] = [];
        //         }
        //         splits[arrowStartId].push(arrowEndId);
        //     }else{
        //         if (!(arrowEndId in merges)) {
        //             merges[arrowEndId] = [];
        //         }
        //         merges[arrowEndId].push(arrowStartId);
        //     }
        // });

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
                "initial-column": Number(bind),
                "target-column": binds[bind][0]
            };
            console.log(res);
            actionObj.actions.push(res);
        });

        Object.keys(splits).forEach(split => {
            let res = {
                "type": "split",
                "initial-column": Number(split),
                "target-columns": splits[split],
                "pattern": Array(splits[split].length).fill("(\\S+)").join(" ")
            };
            console.log(res);
            actionObj.actions.push(res);
        });

        Object.keys(merges).forEach(merge => {
            let res = {
                "type": "merge",
                "initial-columns": merges[merge],
                "target-column": Number(merge),
                "pattern": merges[merge].map(number => "$" + number).join(' ')
            };
            console.log(res);
            actionObj.actions.push(res);
        });

        console.log(JSON.stringify(actionObj));
        setRequestBody(JSON.stringify(actionObj));
        setShouldRedirect(true);



        // Object.keys(binds).forEach(bind => {
        //    let res = {
        //        "type": "bind",
        //        "initial-column": findIndexOfXByIdOfY(columnsFromFile, bind)[0],
        //        "target-column": findIndexOfXByIdOfY(columnsToFile, binds[bind][0])[0]
        //    };
        //    console.log(res);
        // });
        //
        // Object.keys(splits).forEach(split => {
        //     let res = {
        //         "type": "split",
        //         "initial-column": findIndexOfXByIdOfY(columnsFromFile, split)[0],
        //         "target-columns": findIndexOfXByIdOfY(columnsToFile, splits[split]),
        //         "pattern": Array(findIndexOfXByIdOfY(columnsToFile, splits[split][0]).length).fill("(\\S+)").join(" ")
        //     };
        //     console.log(res);
        // });

        // arrows.forEach(arrow => {
        //
        //     let arrowStartId = arrow[0];
        //     let arrowEndId = arrow[1];
        //     if(arrow[2].type === 'connect') {
        //         console.log('=================================');
        //         console.log('arrow start:',arrow[0]);
        //         console.log('arrow end:',arrow[1]);
        //         console.log('arrow start index:', findIndexOfXByIdOfY(columnsFromFile, arrowStartId)[0]);
        //         console.log('arrow end index:', findIndexOfXByIdOfY(columnsToFile, arrowEndId)[0]);
        //     }
        //
        //     if(arrow[2].type === 'split') {
        //         console.log('=================================');
        //         console.log('arrow start:',arrow[0]);
        //         console.log('arrow end:',arrow[1]);
        //         console.log('arrow start index:', findIndexOfXByIdOfY(columnsFromFile, arrowStartId));
        //         console.log('arrow end index:', findIndexOfXByIdOfY(columnsToFile, arrowEndId));
        //     }
        // });

        // columnsFromFile.forEach(column => {
        //     let colIndex = i++;
        //     let colName = column[0];
        //     let colId = column[1];
        //
        //     arrows.forEach(arrow => {
        //         if(arrow[0] === colId) {
        //             console.log('===================');
        //             console.log('arrow obj:', arrow);
        //             console.log('arrow start:',arrow[0]);
        //             console.log('arrow end:',arrow[1]);
        //             console.log('operation type:', arrow[2].type);
        //             console.log('arrow target index:', findConnectTargetIndexByTargetId(arrow[1]));
        //             console.log('arrow start index:', colIndex);
        //             console.log('start column name:', colName);
        //             console.log('start column id:', colId);
        //
        //             if(arrow[2].type === 'connect'){
        //                 let res  = {
        //                     "type": "bind",
        //                     "initial-column": colIndex,
        //                     "target-column": findConnectTargetIndexByTargetId(arrow[1])
        //                 };
        //             }
        //
        //             if(arrow[2].type === 'split'){
        //                 let res  = {
        //                     "type": "split",
        //                     "initial-column": colIndex,
        //                     "target-columns": findSplitTargetsIndexByTargetId(arrow[1]),
        //                     "pattern": Array(findConnectTargetIndexByTargetId(arrow[1]).length).fill("(\\S+)").join(" ")
        //                 };
        //             }
        //
        //             if(arrow[2].type === 'merge'){
        //                 let res  = {
        //                     "type": "merge",
        //                     "initial-columns": colIndex,
        //                     "target-columns": findSplitTargetsIndexByTargetId(arrow[0])[0],
        //                     "pattern": Array(findSplitTargetsIndexByTargetId(arrow[0]).length).fill("$3").join(" ")
        //                 };
        //             }
        //         }
        //     });
        // });

        // var formdata = new FormData();
        // formdata.append("conversion", JSON.stringify(actionObj));
        // formdata.append("master-file", "0");
        // formdata.append("first-file", fileFrom, fileFrom.name);
        // formdata.append("second-file", fileTo, fileTo.name);
        //
        // var requestOptions = {
        //     method: 'POST',
        //     body: formdata,
        //     redirect: 'follow'
        // };
        //
        // fetch("http://127.0.0.1:8080/api/convert", requestOptions)
        //     .then(response => response.text())
        //     .then(result => console.log(result))
        //     .catch(error => console.log('error', error));
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

    return (
        <div className={css.page}>
            <div className={css.header}>
                <div className={css.header_text_block}>
                    <h1 className={css.header_header}>Изменение структуры</h1>
                    <p className={css.header_description}>Вам предстоит выбрать как будет выглядеть Ваш документ после
                        конвертации</p>
                </div>
                <img src={upload_image}/>
            </div>
            <div className={css.edit}>
                <div className={css.scheme_board} onMouseMove={mouseMoveHandler}>
                    <Xwrapper>
                        <div className={css.dynamic_arrow_endpoint} id='dynamic_arrow_endpoint'>

                        </div>
                        <div className={css.file_struct}>
                            <h1 className={css.struct_header}>Исходная структура</h1>
                            {/*<div className={css.struct_block} id={'init-' + useId()} onClick={structBlockOutArrowHandler}>*/}
                            {/*    <input className={css.struct_input} type='text' value='ФИО'/>*/}
                            {/*</div>*/}
                            {/*<div className={css.struct_block} id={'init-' + useId()} onClick={structBlockOutArrowHandler}>*/}
                            {/*    <input className={css.struct_input} type='text' value='Дата рождения'/>*/}
                            {/*</div>*/}
                            {/*<div className={css.struct_block} id={'init-' + useId()} onClick={structBlockOutArrowHandler}>*/}
                            {/*    <input className={css.struct_input} type='text' value='Возраст'/>*/}
                            {/*</div>*/}
                            {/*<div className={css.struct_block} id={'init-' + useId()} onClick={structBlockOutArrowHandler}>*/}
                            {/*    <input className={css.struct_input} type='text' value='Диагноз (название)'/>*/}
                            {/*</div>*/}
                            {/*<div className={css.struct_block} id={'init-' + useId()} onClick={structBlockOutArrowHandler}>*/}
                            {/*    <input className={css.struct_input} type='text' value='Диагноз (код)'/>*/}
                            {/*</div>*/}
                            {columnsFromFile.map(columnName =>
                                <div className={css.struct_block} id={columnName[1]} onClick={structBlockOutArrowHandler}>
                                    <p className={css.struct_input}>{columnName[0]}</p>
                                </div>
                            )}
                        </div>
                        <div className={css.file_struct}>
                            <h1 className={css.struct_header}>Необходимая структура</h1>
                            {/*<div className={css.struct_block} id={'wanted-' + useId()}*/}
                            {/*     onClick={structBlockInArrowHandler}>*/}
                            {/*    <input className={css.struct_input} type='text' value='Фамилия'/>*/}
                            {/*</div>*/}
                            {/*<div className={css.struct_block} id={'wanted-' + useId()}*/}
                            {/*     onClick={structBlockInArrowHandler}>*/}
                            {/*    <input className={css.struct_input} type='text' value='Имя'/>*/}
                            {/*</div>*/}
                            {/*<div className={css.struct_block} id={'wanted-' + useId()}*/}
                            {/*     onClick={structBlockInArrowHandler}>*/}
                            {/*    <input className={css.struct_input} type='text' value='Отчество'/>*/}
                            {/*</div>*/}
                            {/*<div className={css.struct_block} id={'wanted-' + useId()}*/}
                            {/*     onClick={structBlockInArrowHandler}>*/}
                            {/*    <input className={css.struct_input} type='text' value='Возраст'/>*/}
                            {/*</div>*/}
                            {/*<div className={css.struct_block} id={'wanted-' + useId()}*/}
                            {/*     onClick={structBlockInArrowHandler}>*/}
                            {/*    <input className={css.struct_input} type='text' value='Диагноз'/>*/}
                            {/*</div>*/}
                            {columnsToFile.map(columnName =>
                                <div className={css.struct_block} id={columnName[1]} onClick={structBlockInArrowHandler}>
                                    <p className={css.struct_input}>{columnName[0]}</p>
                                </div>
                            )}
                        </div>
                        {arrows.map(startAndEnd => <Xarrow key={startAndEnd[3]} start={startAndEnd[0]}
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