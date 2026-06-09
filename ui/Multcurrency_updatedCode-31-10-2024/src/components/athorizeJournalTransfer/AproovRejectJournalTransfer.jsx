/* eslint-disable no-undef */
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import { useNavigate } from "react-router-dom";
import swal from "sweetalert";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
export default function AproovRejectJournalTransfer() {
  const navigate = useNavigate();
  const [approverejectAlldata, setapproverejectAlldata] = useState([]);
  const [selectedItemId, setSelectedItemId] = useState(null);
  const [newstrTsnId, setNewStrTxnId] = useState("");
  const [newstrFromAccountType, setStrFromAccountType] = useState("");
  const [newstrToAccountType, setStrToAccountType] = useState();
  const [newstrFromAccountNumber, setStrFromAccountNumber] = useState("");
  const [newstrToAccountNumber, setStrToAccountNumber] = useState("");
  const [newstrAmoutToTransfer, setStrAmoutToTransfer] = useState("");
  const [newstrRejectReason, setStrRejectReason] = useState("");
  const [newstrTxnStatus, setstrTxnStatus] = useState("");
  const [newtxnJournalTransferType, setTxnJournalTransferType] = useState("");
  const [checkedHnadler, setCheckedHnadler] = useState(false);
  const [itemPage, setItemPage] = useState(10);
  const [tcount, setTcount] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState("");

  let userid = localStorage.getItem("userName");
  // const [searchKeyword, setSearchKeyword] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);

  // Function to handle changes in the search input field
  const handleSearch = (event) => {
    setSearchKeyword(event.target.value);
  };
  // Filter the acceptData array based on the searchKeyword
  const filteredData = approverejectAlldata.filter((data) => {
    const {
      strFromAccountType,
      strFromAccountNumber,
      strFromAccountName,
      strToAccountType,
      strToAccountNumber,
      strToAccountName,
      // strAmoutToTransfer,
    } = data;
    const lowerCasedSearchKeyword = searchKeyword.toLowerCase();

    return (
      (strFromAccountType &&
        strFromAccountType.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strFromAccountNumber &&
        strFromAccountNumber.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strFromAccountName &&
        strFromAccountName.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strToAccountType &&
        strToAccountType.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strToAccountNumber &&
        strToAccountNumber.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strToAccountName &&
        strToAccountName.toLowerCase().includes(lowerCasedSearchKeyword))
      // (strAmoutToTransfer &&
      //   strAmoutToTransfer.toLowerCase().includes(lowerCasedSearchKeyword))
    );
  });
  const highlightKeyword = (text, keyword) => {
    if (!keyword || typeof text !== "string") {
      return text; // No keyword to highlight or invalid text
    }

    const regex = new RegExp(`(${escapeRegExp(keyword)})`, "gi");
    const highlightedText = text.replace(
      regex,
      '<span class="bg-yellow-200">$1</span>'
    );
    return <div dangerouslySetInnerHTML={{ __html: highlightedText }} />;
  };
  const escapeRegExp = (string) => {
    return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"); // Escape special characters
  };

  const handleCheckboxChange = (data) => {
    setCheckedHnadler(true);
    setSelectedItemId(data);
    setNewStrTxnId(data.strTxnId);
    setStrFromAccountType(data.strFromAccountType);
    setStrToAccountType(data.strToAccountType);
    setStrFromAccountNumber(data.strFromAccountNumber);
    setStrToAccountNumber(data.strToAccountNumber);
    setStrAmoutToTransfer(data.strAmoutToTransfer);
    setStrRejectReason(data.strRejectReason);
    setstrTxnStatus(data.strTxnStatus);
    setTxnJournalTransferType(data.txnJournalTransferType);
  };

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  const { strTxnId } = useParams();
  useEffect(() => {
    approvereject();
  }, []);

  const approvereject = async () => {
    setCheckedHnadler(false);
    try {
      const response = await amsApi.post(
        `journalTransfer/getTxnIdAuthoriselst`,
        {
          strTxnId: strTxnId,
        }
      );
      if (response.data.code === "S0000") {
        setapproverejectAlldata(response.data.journalTransferList);
        setCheckpoint(1);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(
      //   "Sorry, the server is under mentaines. Please try again later"
      // );
    }
  };
  const Approve = async () => {
    if (
      checkedHnadler === false ||
      newstrFromAccountType === "" ||
      newstrTsnId === "" ||
      newstrToAccountType === "" ||
      newstrFromAccountNumber === "" ||
      newstrToAccountNumber === "" ||
      newstrAmoutToTransfer === "" ||
      newstrRejectReason === "" ||
      newtxnJournalTransferType === "" ||
      userid === ""
    ) {
      swal("Please slect checkbox");
    } else {
      try {
        const response = await amsApi.post(`journalTransfer/aprove`, {
          strTxnId: newstrTsnId,
          strFromAccountType: newstrFromAccountType,
          strToAccountType: newstrToAccountType,
          strFromAccountNumber: newstrFromAccountNumber,
          strToAccountNumber: newstrToAccountNumber,
          strAmoutToTransfer: newstrAmoutToTransfer,
          strRejectReason: newstrRejectReason,
          strTxnStatus: "Approved",
          txnJournalTransferType: newtxnJournalTransferType,
          strCheckerId: userid,
        });
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          navigate(-1);
        } else {
          handleShowSuccess(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  const reject = async () => {
    if (checkedHnadler === false) {
      swal("Please slect checkbox");
    } else {
      try {
        const response = await amsApi.post(
          `journalTransfer/rejectJornalTransfer`,
          {
            strTxnId: newstrTsnId,
            strFromAccountType: newstrFromAccountType,
            strToAccountType: newstrToAccountType,
            strFromAccountNumber: newstrFromAccountNumber,
            strToAccountNumber: newstrToAccountNumber,
            strAmoutToTransfer: newstrAmoutToTransfer,
            strRejectReason: newstrRejectReason,
            strTxnStatus: "Rejected",
            txnJournalTransferType: newtxnJournalTransferType,
          }
        );
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          navigate(-1);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xl text-black :text-2xl  leading-normal ">
                Authorize Journal Transfer
              </p>
              <div className=" flex justify-center rounded-md border border-transparent  px-5 text-sm font-medium">
                <div className="pt-2 relative  mx-auto text-gray-600">
                  <input
                    type="search"
                    id="search"
                    value={searchKeyword}
                    onChange={handleSearch}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Search.."
                    style={{
                      backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                    }}
                    disabled={checkpoint !== 1}
                  />{" "}
                  <button
                    title=""
                    type="submit"
                    className="absolute right-0 top-0 mt-3  px-2  "
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                      strokeWidth={1.5}
                      stroke="currentColor"
                      className="w-6 h-6"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                      />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={approverejectAlldata[0]?.strTotalCount}
          reCallApi={approvereject}
        />

        {/* table */}
        <div className="bg-white overflow-x-auto relative  ">
          <div className="table-wrp block lg:max-h-[33rem] max-w-[27rem]">
            <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
              <thead className="text-xm border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                <tr>
                  <th scope="col" className="py-2.5 px-8 whitespace-nowrap">
                    SELECT
                  </th>
                  <th scope="col" className="py-2.5 px-8 whitespace-nowrap">
                    FROM A/C TYPE
                  </th>
                  <th scope="col" className="py-2.5 px-8 whitespace-nowrap">
                    FROM A/C NUMBER
                  </th>
                  <th scope="col" className="py-2.5 px-8 whitespace-nowrap">
                    FROM A/C NAME
                  </th>
                  <th scope="col" className="py-2.5 px-8 whitespace-nowrap">
                    TO A/C TYPE
                  </th>
                  <th scope="col" className="py-2.5 px-8 whitespace-nowrap">
                    TO A/C NUMBER
                  </th>
                  <th scope="col" className="py-2.5 px-8 whitespace-nowrap">
                    TO A/C NAME
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    AMOUNT TO TRANSFER
                  </th>

                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    NARRATION
                  </th>
                </tr>
              </thead>
              <tbody>
                {filteredData.length > 0 ? (
                  <>
                    {filteredData?.map((data) => (
                      <tr
                        key={uuidv4()}
                        className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                      >
                        <td className="px-6 py-2.5 whitespace-nowrap">
                          <div className="form-control">
                            <label className="cursor-pointer label">
                              <input
                                input
                                type="checkbox"
                                checked={selectedItemId === data}
                                onChange={() => handleCheckboxChange(data)}
                                className="checkbox checkbox-info  checkbox-sm"
                              />
                            </label>
                          </div>
                        </td>
                        <td className="px-6 py-2.5 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {highlightKeyword(
                              data.strFromAccountType || "-",
                              searchKeyword
                            )}
                          </span>
                        </td>
                        <td className="px-6 py-2.5 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {highlightKeyword(
                              data.strFromAccountNumber || "-",
                              searchKeyword
                            )}
                          </span>
                        </td>
                        <td className="px-6 py-2.5 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {highlightKeyword(
                              data.strFromAccountName || "-",
                              searchKeyword
                            )}
                          </span>
                        </td>
                        <td className="px-6 py-2.5 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {highlightKeyword(
                              data.strToAccountType || "-",
                              searchKeyword
                            )}
                          </span>
                        </td>
                        <td className="px-6 py-2.5 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {highlightKeyword(
                              data.strToAccountNumber || "-",
                              searchKeyword
                            )}
                          </span>
                        </td>
                        <td className="px-6 py-2.5 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {highlightKeyword(
                              data.strToAccountName || "-",
                              searchKeyword
                            )}
                          </span>
                        </td>
                        <td className="px-6 py-2.5 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {highlightKeyword(
                              data.strAmoutToTransfer || "-",
                              searchKeyword
                            )}
                          </span>
                        </td>
                        <td className="px-6 py-2.5 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {highlightKeyword(
                              data.strNarration || "-",
                              searchKeyword
                            )}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </>
                ) : (
                  <tr>
                    <td colSpan="6" className="px-6 py-1 text-center">
                      <span className="text-sm font-normal text-gray-500">
                        No data available.
                      </span>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>

        <div className="bg-gray-100 px-2 py-1 text-right sm:px-4 ">
          <button
            type="button"
            onClick={Approve}
            className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
          >
            Approve
          </button>
          <button
            type="button"
            onClick={reject}
            className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
          >
            Reject
          </button>
          <button
            title=" Previous  page"
            type="button"
            onClick={() => navigate(-1)}
            className="bg-gray-500 hover:bg-red-700 focus:ring-2 focus:ring-gray-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-gray-700 rounded"
          >
            BACK
          </button>
        </div>
      </div>

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
