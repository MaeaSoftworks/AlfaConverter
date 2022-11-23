import css from "../Result/Result.module.css";
import upload_img from "../Upload/upload_image.png";
import {Link, useLocation} from 'react-router-dom';
import back_arrow_img from './back_arrow.svg';


const Result = () => {

    const {state} = useLocation();
    const requestBody = state.requestBody;
    const fileFrom = state.fileFrom;
    const fileTo = state.fileTo;

    const download = () => {
        var formdata = new FormData();
        formdata.append("conversion", requestBody);
        formdata.append("master-file", "0");
        formdata.append("first-file", fileFrom, fileFrom.name);
        formdata.append("second-file", fileTo, fileTo.name);

        var requestOptions = {
            method: 'POST',
            body: formdata,
            redirect: 'follow'
        };

        fetch("http://127.0.0.1:8080/api/convert", requestOptions)
            .then(response => response.blob())
            .then(result => {
                console.log(result);
                const url = window.URL.createObjectURL(new Blob([result]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', `${Date.now()}.xlsx`);
                document.body.appendChild(link);
                link.click();
            })
            .catch(error => console.log('error', error));
    };

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
                        <button className={css.send_button} onClick={download}>Скачать файл</button>
                    </div>
                </div>
                <img src={upload_img} className={css.image}/>
            </div>
        </div>
    );
};

export default Result;