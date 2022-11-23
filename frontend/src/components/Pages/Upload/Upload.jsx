import css from './Upload.module.css';
import { useNavigate } from "react-router-dom";
import upload_img from './upload_image.png';
import React, {useState} from 'react';
import result from "../Result/Result";

const Upload = () => {

    const [shouldRedirect, setShouldRedirect] = useState(false);
    let documentColumns = [];
    let [docColumns, setDocColumns] = useState([]);
    let [fileFrom, setFileFrom] = useState();
    let [fileTo, setFileTo] = useState();

    const navigate = useNavigate();

    React.useEffect(() => {
        if (shouldRedirect) {
            navigate('/edit', { state: { columns: docColumns, fileTo: fileTo, fileFrom: fileFrom} });
        }
    });

    const onInput = (data) => {
        // data.preventDefault();
        // console.log(data.target.files);
    };

    const uniqueId = (prefix = 'id-') => prefix + Math.random().toString(16).slice(-12);

    // const onSubmit = async (data) => {
    //     data.preventDefault();
    //
    //     const fileFrom = data.target[0].files[0];
    //     const fileTo = data.target[1].files[0];
    //
    //     const formData = new FormData();
    //     formData.append("first-file", fileFrom);
    //     formData.append("second-file", fileTo);
    //     formData.append("master-file", '0');
    //     formData.append("conversion", "{\"actions\":[{\"type\":\"bind\",\"initial-column\":0,\"target-column\":1},{\"type\":\"bind\",\"initial-column\":1,\"target-column\":0},{\"type\":\"split\",\"initial-column\":2,\"target-columns\":[2,3,4],\"pattern\":\"(\\\\S+) (\\\\S+) (\\\\S+)\"},{\"type\":\"merge\",\"initial-columns\":[3,4,5],\"target-column\":5,\"pattern\":\"$3 $4 $5\"}]}");
    //
    //     const requestOptions = {
    //         method: 'POST',
    //         body: formData,
    //     };
    //
    //     fetch("http://127.0.0.1:8080/api/convert", requestOptions)
    //         .then(response => response.text())
    //         .then(result => console.log(result))
    //         .catch(error => console.log('error', error));
    // };

    const onSubmit = async (data) => {
        data.preventDefault();

        const fileFrom = data.target[0].files[0];
        const fileTo = data.target[1].files[0];

        const formData = new FormData();
        formData.append("first-file", fileFrom);
        formData.append("second-file", fileTo);
        setFileFrom(fileFrom);
        setFileTo(fileTo);

        const requestOptions = {
            method: 'POST',
            body: formData
        };

        fetch("http://127.0.0.1:8080/api/headers", requestOptions)
            .then(response => response.json())
            .then(result => {
                console.log('result:');
                console.log(result);
                console.log(result[0]);
                console.log(result[1]);
                result[0] = result[0].map(columnFromName => [columnFromName, uniqueId('init-')]);
                result[1] = result[1].map(columnFromName => [columnFromName, uniqueId('wanted-')]);
                console.log(result);
                setDocColumns(result);
                setShouldRedirect(true);
            })
            .catch(error => console.log('error', error));
    };

    return (
        <div className={css.page}>
            <div className={css.upload}>
                <div>
                    <h1 className={css.header}>Конвертация</h1>
                    <p className={css.description}>Сервис поддерживает файлы объемом до 20МБ</p>
                    <form onSubmit={onSubmit} className={css.form}>

                        <div className={css.file_upload_block}>
                            <label htmlFor="source_file" className={css.file_input_label}>
                                Выбрать файл
                            </label>
                            <p className={css.file_input_description}>Выберите исходный файл</p>
                            {/*<input type="file" name="source_file" id="source_file" className={css.file_input}/>*/}
                            <input type="file" id="source_file" className={css.file_input} onChange={onInput}/>
                        </div>

                        <div className={css.file_upload_block}>
                            <label htmlFor="template_file" className={css.file_input_label}>
                                Выбрать файл
                            </label>
                            <p className={css.file_input_description}>Выберите файл, с необходимой структурой</p>
                            {/*<input type="file" name="template_file" id="template_file" className={css.file_input}/>*/}
                            <input type="file" id="template_file" className={css.file_input} onChange={onInput}/>
                        </div>

                        <input type="submit" value="Отправить" className={css.send_button}/>
                    </form>
                </div>
                <img src={upload_img} className={css.image}/>
            </div>
        </div>
    );
};

export default Upload;