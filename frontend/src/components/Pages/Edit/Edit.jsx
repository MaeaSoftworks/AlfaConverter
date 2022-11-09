import css from './Edit.module.css';
import upload_image from './upload_image.png';
import r0 from './Rectangle 50.png';
import r2 from './Rectangle 52.png';
import r3 from './Rectangle 53.png';
import r4 from './Rectangle 54.png';
import {Link} from "react-router-dom";

const Edit = () => {
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
                    <div className={css.file_struct}>
                        <h1 className={css.struct_header}>Исходная структура</h1>
                        <div className={css.struct_block}>
                            <input className={css.struct_input} type='text' value='ФИО'/>
                        </div>
                        <div className={css.struct_block}>
                            <input className={css.struct_input} type='text' value='Дата рождения'/>
                        </div>
                        <div className={css.struct_block}>
                            <input className={css.struct_input} type='text' value='Возраст'/>
                        </div>
                        <div className={css.struct_block}>
                            <input className={css.struct_input} type='text' value='Адрес прописки пациента'/>
                        </div>
                        <div className={css.struct_block}>
                            <input className={css.struct_input} type='text' value='Диагноз (код)'/>
                        </div>
                    </div>
                    <div className={css.file_struct}>
                        <h1 className={css.struct_header}>Необходимая структура</h1>
                        <div className={css.struct_block}>
                            <input className={css.struct_input} type='text' value='ФИО'/>
                        </div>
                        <div className={css.struct_block}>
                            <input className={css.struct_input} type='text' value='Дата рождения'/>
                        </div>
                        <div className={css.struct_block}>
                            <input className={css.struct_input} type='text' value='Возраст'/>
                        </div>
                        <div className={css.struct_block}>
                            <input className={css.struct_input} type='text' value='Адрес прописки пациента'/>
                        </div>
                        <div className={css.struct_block}>
                            <input className={css.struct_input} type='text' value='Диагноз (код)'/>
                        </div>
                    </div>
                </div>
                <div className={css.control_panel}>
                    <h1 className={css.control_panel_header}>Инструменты</h1>
                    <ul className={css.controls}>
                        <li className={css.control}>
                            <button className={css.control_button}>
                                <img src={r0} className={css.control_button_img}/>
                            </button>
                            <div>
                                <h2 className={css.control_name}>Связь между столбцами</h2>
                                <p className={css.control_description}>Используйте для связи столбцов, к которым хотите
                                    применить изменение</p>
                            </div>
                        </li>
                        <li className={css.control}>
                            <button className={css.control_button}>
                                <img src={r2}/>
                            </button>
                            <div>
                                <h2 className={css.control_name}>Разделение</h2>
                                <p className={css.control_description}>Выберите стрелку, к которой должно быть применено
                                    данное действие</p>
                            </div>
                        </li>
                        <li className={css.control}>
                            <button className={css.control_button}>
                                <img src={r3}/>
                            </button>
                            <div>
                                <h2 className={css.control_name}>Слияние</h2>
                                <p className={css.control_description}>Выберите стрелку, к которой должно быть применено
                                    данное действие</p>
                            </div>
                        </li>
                        <li className={css.control}>
                            <button className={css.control_button}>
                                <img src={r4} className={css.control_button_img}/>
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