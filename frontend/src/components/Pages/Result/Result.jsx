import css from "../Result/Result.module.css";
import upload_img from "../Upload/upload_image.png";
import {Link, useLocation} from 'react-router-dom';
import back_arrow_img from './back_arrow.svg';
import {useState} from "react";


const Result = () => {

    const {state} = useLocation();
    const requestBody = state.requestBody;
    const fileFrom = state.fileFrom;
    const fileTo = state.fileTo;
    const [isResponseOk, setIsResponseOk] = useState(true);

    const download = () => {
        var formdata = new FormData();
        formdata.append("conversion", requestBody);
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

    return (
        <div className={css.page}>
            <div className={css.upload}>
                <div>
                    <h1 className={css.header}>Ваш файл успешно преобразован</h1>
                    <p className={css.description}>Итоговый вариант доступен для скачивания</p>
                    <div className={css.buttons}>
                        <Link to="/">
                            <img className={css.back_arrow} src={back_arrow_img}/>
                        </Link>
                        <button className={css.send_button} onClick={download}>Скачать файл</button>
                    </div>
                    <p className={isResponseOk ? css.server_error_hidden : css.server_error_showed}>Ошибка во время
                        обработки файла на сервере. Возможно, вы указали слишком большое количество разделителей</p>
                </div>
                <img src={upload_img} className={css.image}/>
            </div>
        </div>
    );
};

export default Result;