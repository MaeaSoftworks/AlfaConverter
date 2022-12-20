import css from './UploadXlsxToXlsx.module.css';
import {useNavigate} from "react-router-dom";
import upload_img from './upload_image.png';
import React, {useState, useEffect} from 'react';
import result from "../ResultXlsxToJson/ResultXlsxlToJson";

const UploadXlsxToXlsx = () => {

    const [shouldRedirect, setShouldRedirect] = useState(false);
    let documentColumns = [];
    let [docColumns, setDocColumns] = useState([]);
    let [zipped, setZipped] = useState([]);
    let [fileFrom, setFileFrom] = useState();
    let [fileTo, setFileTo] = useState();

    const [fileFromErrorClass, setFileFromErrorClass] = useState([css.file_error_hidden, css.file_error_hidden]);
    const [fileToErrorClass, setFileToErrorClass] = useState([css.file_error_hidden, css.file_error_hidden]);


    const [isFileFromLegit, setIsFileFromLegit] = useState(false);
    const [isFileToLegit, setIsFileToLegit] = useState(false);
    const [isInputReady, setIsInputReady] = useState(false);
    const [isResponseOk, setIsResponseOk] = useState(true);

    const navigate = useNavigate();

    useEffect(() => {
        if (shouldRedirect) {
            navigate('/edit', {state: {columns: docColumns, fileTo: fileTo, fileFrom: fileFrom, zipped: zipped}});
        }
    });

    useEffect(() => {
        setIsInputReady(isFileFromLegit && isFileToLegit);
    }, [isFileFromLegit, isFileToLegit]);

    const onInputFileFrom = (data) => {
        // data.preventDefault();
        handleFileInputErrors(data, fileFromErrorClass, setFileFromErrorClass, setIsFileFromLegit);
    };

    const onInputFileTo = (data) => {
        // data.preventDefault()
        handleFileInputErrors(data, fileToErrorClass, setFileToErrorClass, setIsFileToLegit);
    };

    const handleFileInputErrors = (inputData, fileErrorClass, setFileErrorClass, setIsFileLegit) => {
        // console.log(inputData.target.files[0].name);
        let fileExtension = inputData.target.files[0].name.split('.').pop();
        let fileSize = inputData.target.files[0].size;
        // console.log(fileExtension);

        let isFileExtensionLegit;
        let isFileSizeLegit;

        if (fileExtension !== 'xlsx') {
            let errorsClasses = fileErrorClass;
            errorsClasses[0] = css.file_error_showed;
            setFileErrorClass([...errorsClasses]);
            isFileExtensionLegit = false;
        } else {
            let errorsClasses = fileErrorClass;
            errorsClasses[0] = css.file_error_hidden;
            setFileErrorClass([...errorsClasses]);
            isFileExtensionLegit = true;
        }

        if (fileSize > 20971520) {
            let errorsClasses = fileErrorClass;
            errorsClasses[1] = css.file_error_showed;
            setFileErrorClass([...errorsClasses]);
            isFileSizeLegit = false;
        } else {
            let errorsClasses = fileErrorClass;
            errorsClasses[1] = css.file_error_hidden;
            setFileErrorClass([...errorsClasses]);
            isFileSizeLegit = true;
        }

        setIsFileLegit(isFileExtensionLegit && isFileSizeLegit);
    };

    const zip = (keys, values) => {
        let obj = {};
        keys.forEach(key => obj[key] = values.shift());
        return obj;
    };

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
        setIsResponseOk(true);

        const fileFrom = data.target[0].files[0];
        const fileTo = data.target[1].files[0];

        const formData = new FormData();
        formData.append("source", fileFrom);
        formData.append("modifier", fileTo);
        setFileFrom(fileFrom);
        setFileTo(fileTo);

        const requestOptions = {
            method: 'POST',
            body: formData
        };

        let status = 200;

        fetch("http://127.0.0.1:8080/api/xlsx/preview", requestOptions)
            .then(response => {
                status = response.status;
                return response.json();
            })
            .then(result => {
                console.log('result:');
                console.log(result);
                if (status === 200) {
                    let firstFileColumns = result[0].headers;
                    let secondFileColumns = result[1].headers;
                    console.log('result');
                    console.log(result);
                    console.log('zip');
                    setZipped(zip(firstFileColumns, [...result[0]["examples"]]));
                    console.log(zipped);
                    console.log('firstFileColumns');
                    console.log(firstFileColumns);
                    console.log('vals');
                    console.log(result[0]["examples"]);
                    console.log('secondFileColumns');
                    console.log(secondFileColumns);
                    firstFileColumns = firstFileColumns.map((columnFromName, index) => [columnFromName, `from-${index}`]);
                    secondFileColumns = secondFileColumns.map((columnFromName, index) => [columnFromName, `to-${index}`]);
                    console.log(result);
                    setDocColumns([firstFileColumns, secondFileColumns]);
                    setIsResponseOk(true);
                    setShouldRedirect(true);
                    console.log(shouldRedirect);
                } else if (status === 500) {
                    setIsResponseOk(false);
                }
            })
            .catch(error => console.log('error', error));
    };

    return (
        <div className={css.page}>
            <div className={css.upload}>
                <div className={css.textblock}>
                    <h1 className={css.header}>Конвертация</h1>
                    <p className={css.description}>Загрузите два файла: документ, который нужно конвертировать и
                        документ с необходимой структурой. Сервис поддерживает файлы объемом до 20МБ</p>
                </div>
                <form onSubmit={onSubmit} className={css.form}>

                    <div className={css.file_button_container}>
                        <label htmlFor="source_file" className={`${css.file_input_label} ${isFileFromLegit ? css.file_input_label_loaded : ''}`}>
                            Выбрать исходный файл
                        </label>
                        <p className={css.file_input_description}>или перетащите файл сюда</p>
                        {/*<input type="file" name="source_file" id="source_file" className={css.file_input}/>*/}
                        <input type="file" id="source_file" className={css.file_input} onChange={onInputFileFrom}/>
                    </div>
                    <p className={fileFromErrorClass[0]}>Ошибка! Файл имеет неверное расширение. Необходимо
                        .xlsx</p>
                    <p className={fileFromErrorClass[1]}>Ошибка! Файл имеет слишком большой размер. Необходимо 20 МБ
                        или меньше</p>

                    <div className={css.file_button_container}>
                        <label htmlFor="template_file" className={`${css.file_input_label} ${isFileToLegit ? css.file_input_label_loaded : ''}`}>
                            Выбрать структуру
                        </label>
                        <p className={css.file_input_description}>или перетащите файл сюда</p>
                        {/*<input type="file" name="template_file" id="template_file" className={css.file_input}/>*/}
                        <input type="file" id="template_file" className={css.file_input} onChange={onInputFileTo}/>
                    </div>
                    <p className={fileToErrorClass[0]}>Ошибка! Файл имеет неверное расширение. Необходимо .xlsx</p>
                    <p className={fileToErrorClass[1]}>Ошибка! Файл имеет слишком большой размер. Необходимо 20 МБ
                        или меньше</p>

                    <div className={css.file_button_container}>
                        <input type="submit" value="Перейти к редактированию файлов "
                               className={`${css.send_button} ${isInputReady ? css.button_active : css.button_inactive}`}
                               disabled={!isInputReady}/>
                    </div>
                </form>
                <p className={isResponseOk ? css.server_error_hidden : css.server_error_showed}>Произошла ошибка на
                    сервере. Попробуйте снова</p>
            </div>
        </div>
    );
};

export default UploadXlsxToXlsx;