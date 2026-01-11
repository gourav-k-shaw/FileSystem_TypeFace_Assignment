import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/files";

export const fetchFiles = async () => {
    const res = await axios.get(API_BASE_URL);
    return res.data;
};

export const uploadFile = async (file) => {
    const formData = new FormData();
    formData.append("file", file);

    const res = await axios.post(
        `${API_BASE_URL}/upload`,
        formData,
        { headers: { "Content-Type": "multipart/form-data" } }
    );

    return res.data;
};

export const downloadFile = (id) => {
    window.open(`${API_BASE_URL}/${id}`, "_blank");
    //window.open is used because axios downloads into memory. 
    //window.open streams the file to the browser and leverages the browser's built-in file download
};
