import useFileStore from "../store/fileStore";
import FileItem from "./FileItem";

export default function FileList() {
    const { files } = useFileStore();

    if (!files.length) {
        return (
            <div className="mt-8 text-center p-8 rounded-xl bg-white/5 border border-white/10 border-dashed">
                <svg className="w-16 h-16 mx-auto text-gray-500 mb-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
                </svg>
                <p className="text-gray-400">No files uploaded yet</p>
                <p className="text-gray-500 text-sm mt-1">Upload your first file to get started</p>
            </div>
        );
    }

    return (
        <div className="mt-6 space-y-3">
            {files.map(file => (
                <FileItem key={file.id} file={file} />
            ))}
        </div>
    );
}
