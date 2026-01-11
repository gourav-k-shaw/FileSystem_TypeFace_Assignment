import { useEffect } from "react";
import useFileStore from "../store/fileStore";
import FileList from "../components/FileList";
import UploadFile from "../components/UploadFile";

export default function Home() {
    const { loadFiles } = useFileStore();

    useEffect(() => {
        loadFiles();
    }, []);

    return (
        <div className="w-full max-w-2xl mx-auto p-8 rounded-2xl bg-white/10 backdrop-blur-lg border border-white/20 shadow-2xl">
            <h1 className="text-3xl font-bold mb-2 bg-gradient-to-r from-blue-400 to-purple-400 bg-clip-text text-transparent">My Files</h1>
            <p className="text-gray-300 text-sm mb-6">Upload and manage your files</p>
            <UploadFile />
            <FileList />
        </div>
    );
}
