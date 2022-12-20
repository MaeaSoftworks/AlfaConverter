import css from "../Main/Main.module.css";
import banner_image from "./banner_image.png";
import {NavLink} from "react-router-dom";

const Main = () => {
    return (
        <div className={css.page_body}>
            <div className={css.top_banner}>
                <div className={css.banner_textblock}>
                    <h1 className={css.textblock_header}>Конвертор файлов</h1>
                    <p className={css.textblock_description}>Управлять своими документами стало проще</p>
                </div>
                <img className={css.banner_image} src={banner_image}></img>
            </div>
            <div className={css.choose_block}>
                <NavLink to='/uploadXlsxToXlsx' className={css.choose_element}>
                    <h1 className={css.element_header}>XLSX в XLSX</h1>
                    <p className={css.element_description}>Преобразование ваших таблиц в таблицу с необходимой структурой</p>
                </NavLink>
                <div className={css.vertical_splitter}/>
                <NavLink to='/uploadXlsxToXml' className={css.choose_element}>
                    <h1 className={css.element_header}>XLSX в XML</h1>
                    <p className={css.element_description}>Конвертируйте ваши XLSX файлы в легко редактируемые XML файлы</p>
                </NavLink>
                <div className={css.vertical_splitter}/>
                <NavLink to='/uploadXlsxToJson' className={css.choose_element}>
                    <h1 className={css.element_header}>XLSX в JSON</h1>
                    <p className={css.element_description}>Конвертируйте ваши XLSX файлы в легко редактируемые JSON файлы</p>
                </NavLink>
            </div>
        </div>
    );
};

export default Main;