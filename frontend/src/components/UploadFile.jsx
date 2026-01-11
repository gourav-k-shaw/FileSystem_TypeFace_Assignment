import useFileStore from "../store/fileStore";
import { useState } from "react";

export default function UploadFile() {
    const { addFile } = useFileStore();
    const [file, setFile] = useState(null);

    const handleUpload = async () => {
        if (!file) return;
        await addFile(file);
        setFile(null);
    };

    return (
        <div className="mb-6">
            <div className="flex gap-3 items-center">
                <label className="flex-1 cursor-pointer">
                    <div className="relative overflow-hidden rounded-xl bg-white/5 border border-white/20 hover:border-blue-400/50 transition-all duration-300 p-4 text-center group">
                        <input
                            type="file"
                            onChange={(e) => setFile(e.target.files[0])}
                            className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
                        />
                        <div className="flex items-center justify-center gap-2">
                            <svg className="w-5 h-5 text-blue-400 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
                            </svg>
                            <span className="text-gray-300 group-hover:text-white transition-colors">
                                {file ? file.name : 'Choose File'}
                            </span>
                        </div>
                    </div>
                </label>
                <button
                    onClick={handleUpload}
                    disabled={!file}
                    className="px-6 py-4 rounded-xl bg-gradient-to-r from-blue-500 to-purple-600 text-white font-semibold hover:from-blue-600 hover:to-purple-700 disabled:from-gray-600 disabled:to-gray-700 disabled:cursor-not-allowed transition-all duration-300 shadow-lg hover:shadow-blue-500/50 hover:scale-105 active:scale-95"
                >
                    Upload
                </button>
            </div>
        </div>
    );
}
