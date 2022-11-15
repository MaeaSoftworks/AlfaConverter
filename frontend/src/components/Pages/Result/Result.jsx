import css from "../Result/Result.module.css";
import upload_img from "../Upload/upload_image.png";
import {Link} from 'react-router-dom';
import back_arrow_img from './back_arrow.svg';


const Result = () => {
    return (
        <div className={css.page}>
            <div className={css.upload}>
                <div>
                    <h1 className={css.header}>Ваш файл успешно преобразован</h1>
                    <p className={css.description}>Итоговый вариант доступен для скачивания</p>
                    <div className={css.buttons}>
                        <Link to="/" >
                            <img className={css.back_arrow} src={back_arrow_img}/>
                        </Link>
                        <Link className={css.send_button} to="/Result.xlsx" target="_blank" download>Скачать файл</Link>
                    </div>
                </div>
                <img src={upload_img} className={css.image}/>
            </div>
        </div>
    );
};

export default Result;