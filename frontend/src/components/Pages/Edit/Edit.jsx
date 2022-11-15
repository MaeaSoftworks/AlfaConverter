import css from './Edit.module.css';
import upload_image from './upload_image.png';
import r0 from './Rectangle 50.png';
import r2 from './Rectangle 52.png';
import r3 from './Rectangle 53.png';
import r4 from './Rectangle 54.png';
import {Link} from "react-router-dom";
import Xarrow, {Xwrapper} from "react-xarrows";
import {useId, useState} from "react";

const Edit = () => {

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

    const [currentAction, setCurrentAction] = useState(action.default);
    const [currentStatus, setCurrentStatus] = useState(status.default);
    const [pendingBlockId, setPendingBlockId] = useState('');
    const [clickedButton, setClickedButton] = useState('');

    const [arrows, setArrows] = useState([]);

    const structBlockOutArrowHandler = (event) => {
        console.log(event.target.id);
        let target = event.target;
        console.log(currentStatus);
        if (currentAction.type !== action.default.type) {
            if (currentStatus === status.default) {
                setPendingBlockId(target.id);
                setCurrentStatus(status.pending);
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
                arrows.push([startId, endId, currentAction.arrowColor]);
                setArrows(arrows);
                setPendingBlockId('');
                setCurrentStatus(status.default);
            }
            console.log(arrows);
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
                <div className={css.scheme_board}>
                    <Xwrapper>
                        <div className={css.file_struct}>
                            <h1 className={css.struct_header}>Исходная структура</h1>
                            <div className={css.struct_block} id={'init-' + useId()} onClick={structBlockOutArrowHandler}>
                                <input className={css.struct_input} type='text' value='ФИО'/>
                            </div>
                            <div className={css.struct_block} id={'init-' + useId()} onClick={structBlockOutArrowHandler}>
                                <input className={css.struct_input} type='text' value='Дата рождения'/>
                            </div>
                            <div className={css.struct_block} id={'init-' + useId()} onClick={structBlockOutArrowHandler}>
                                <input className={css.struct_input} type='text' value='Возраст'/>
                            </div>
                            <div className={css.struct_block} id={'init-' + useId()} onClick={structBlockOutArrowHandler}>
                                <input className={css.struct_input} type='text' value='Диагноз (название)'/>
                            </div>
                            <div className={css.struct_block} id={'init-' + useId()} onClick={structBlockOutArrowHandler}>
                                <input className={css.struct_input} type='text' value='Диагноз (код)'/>
                            </div>
                        </div>
                        <div className={css.file_struct}>
                            <h1 className={css.struct_header}>Необходимая структура</h1>
                            <div className={css.struct_block} id={'wanted-' + useId()}
                                 onClick={structBlockInArrowHandler}>
                                <input className={css.struct_input} type='text' value='Фамилия'/>
                            </div>
                            <div className={css.struct_block} id={'wanted-' + useId()}
                                 onClick={structBlockInArrowHandler}>
                                <input className={css.struct_input} type='text' value='Имя'/>
                            </div>
                            <div className={css.struct_block} id={'wanted-' + useId()}
                                 onClick={structBlockInArrowHandler}>
                                <input className={css.struct_input} type='text' value='Отчество'/>
                            </div>
                            <div className={css.struct_block} id={'wanted-' + useId()}
                                 onClick={structBlockInArrowHandler}>
                                <input className={css.struct_input} type='text' value='Возраст'/>
                            </div>
                            <div className={css.struct_block} id={'wanted-' + useId()}
                                 onClick={structBlockInArrowHandler}>
                                <input className={css.struct_input} type='text' value='Диагноз'/>
                            </div>
                        </div>
                        {arrows.map(startAndEnd => <Xarrow kay={startAndEnd[0] + startAndEnd[1]} start={startAndEnd[0]}
                                                           end={startAndEnd[1]}
                                                           color={startAndEnd[2]}
                                                           strokeWidth='4'
                                                           headSize='4'/>)}
                    </Xwrapper>
                </div>
                <div className={css.control_panel}>
                    <h1 className={css.control_panel_header}>Инструменты</h1>
                    <ul className={css.controls}>
                        <li className={css.control}>
                            <button className={css.control_button} onClick={connectOnClick}>
                                <img src={r0} className={clickedButton === 'connect'? css.control_button_active : css.control_button_img}/>
                            </button>
                            <div>
                                <h2 className={css.control_name}>Связь между столбцами</h2>
                                <p className={css.control_description}>Используйте для связи столбцов, к которым хотите
                                    применить изменение</p>
                            </div>
                        </li>
                        <li className={css.control}>
                            <button className={css.control_button} onClick={splitOnClick}>
                                <img src={r2} className={clickedButton === 'split'? css.control_button_active : css.control_button_img}/>
                            </button>
                            <div>
                                <h2 className={css.control_name}>Разделение</h2>
                                <p className={css.control_description}>Выберите стрелку, к которой должно быть применено
                                    данное действие</p>
                            </div>
                        </li>
                        <li className={css.control}>
                            <button className={css.control_button} onClick={mergeOnClick}>
                                <img src={r3} className={clickedButton === 'merge'? css.control_button_active : css.control_button_img}/>
                            </button>
                            <div>
                                <h2 className={css.control_name}>Слияние</h2>
                                <p className={css.control_description}>Выберите стрелку, к которой должно быть применено
                                    данное действие</p>
                            </div>
                        </li>
                        <li className={css.control}>
                            <button className={css.control_button} onClick={deleteOnClick}>
                                <img src={r4} className={clickedButton === 'delete'? css.control_button_active : css.control_button_img}/>
                            </button>
                            <div>
                                <h2 className={css.control_name}>Удалить</h2>
                                <p className={css.control_description}>Выберите действие/связь необходимое к
                                    удалению</p>
                            </div>
                        </li>
                        <li>
                            <Link to='/result'>
                                <button className={css.apply_button}>Преобразовать</button>
                            </Link>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default Edit;