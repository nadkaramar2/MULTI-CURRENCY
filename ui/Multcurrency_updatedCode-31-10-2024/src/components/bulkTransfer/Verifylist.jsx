import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import Swal from "sweetalert2";
import { useParams } from "react-router-dom";
import { v4 as uuidv4 } from "uuid";
import { useNavigate } from "react-router-dom";

import { ArrowLeftIcon } from "@heroicons/react/24/solid";
import CustomAlert from "../../layout/CustomAlert";

import MainPagination from "../../layout/MianPagination";
import swal from "sweetalert";
import AutoPagintation from "../../UI/AutoPagintation";
export default function Verifylist() {
  const [data, setData] = useState([]);
  const [addAmount, setAddAmount] = useState("");
  const navigate = useNavigate();
  const { strBulkMode } = useParams();
  const { strPreTransactionId } = useParams();
  const [searchQuery, setSearchQuery] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);

  const [itemPage, setItemPage] = useState(10);
  const [tcount, setTcount] = useState(0);

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

  useEffect(() => {
    BulkTransferTransactions();
  }, [itemPage]);

  const BulkTransferTransactions = async () => {
    try {
      const response = await amsApi.post(
        `/bulkTransfer/preTxnBulklistForVerify`,
        { strPreTransactionId: strPreTransactionId },

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.status === 200) {
        if (response.data === "Bulk Transfer Not Found") {
          // setCheckpoint(1);
          setData([]);
          // setCheckpoint(1);
        } else {
          setData(response.data);
          var totalChild = response.data.reduce(
            (accum, item) => accum + item.strAmount,
            0
          );
          setAddAmount(totalChild);
        }
      } else {
        handleShowError("Data Not Found");
      }
    } catch (error) {
      handleShowError("Data Not Found");
    }
  };

  const verify = async () => {
    if (!addAmount) {
      swal("Please check your Total Amount To Transfer");
    } else if (!strPreTransactionId || !strBulkMode) {
      swal("Please check your Bulk Mode and participantID ");
    } else {
      try {
        const response = await amsApi.post(
          `bulkTransfer/verify`,
          {
            strPreTransactionId: strPreTransactionId,
            strBulkMode: strBulkMode,
            bulkTransferAmount: addAmount,

            //bulkTransferAmount: "600",
          },

          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          handleShowSuccess("Bulk Transfer verified");

          //  navigate("/authorize", { replace: true });
        } else {
          handleShowError("Bulk Transfer not  verified");
        }
      } catch (error) {
        handleShowError("Bulk Transfer not  verified");
      }
    }
  };

  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-1 ">
          <div className="px-4 sm:px-8 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Pending For Verification List
              </p>
              <button
                title="Go Back"
                type="button"
                onClick={() => navigate(-1)}
                className="inline-flex justify-center  rounded-md border border-transparent bg-gray-500 py-1 px-4 mx-2 text-xs font-sm text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
              >
                <ArrowLeftIcon className="h-3 w-3 " />
              </button>
            </div>
          </div>
        </div>
        {/* <div className="overflow-x-auto relative shadow-md sm:rounded-lg ml-16 "> */}

        {/* <h1 className="font-medium flex justify-center  text-black text-2xl ">
          Pending For Verification List
        </h1> */}

        <div className="flex justify-end rounded-md border border-transparent px-2 mx-2 text-xs font-medium text-white ">
          <div className="pt-2 relative mx-auto text-gray-600">
            {" "}
            <input
              type="search"
              id="search"
              className="block w-full px-3 py-1  text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
              placeholder="Search.."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              // style={{
              //   backgroundColor: checkpoint === 1 ? "white" : "lightgray",
              // }}
              // disabled={checkpoint !== 1}
            />
            <button
              title="Search Data"
              type="submit"
              className="absolute right-0 top-0 mt-2 mr-2"
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
        <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={data[0]?.strTotalCount}
          reCallApi={BulkTransferTransactions}
        />
        <div className="overflow-x-auto relative shadow-md  ">
          <div className="table-wrp block max-h-[27rem] ">
            <table className="w-full text-xs text-left text-black dark:text-blue-100">
              <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                <tr>
                  <th scope="col" className="py-2.5 px-3 whitespace-nowrap">
                    From A/C Type
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap ">
                    From A/C No
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    From A/C Name
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    To A/C Type
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    Top A/C No
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    To A/C Name
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    Amount To Transfer
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    Maker User Id
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap">
                    Narration
                  </th>
                </tr>
              </thead>

              <tbody>
                {data
                  .filter(
                    (data) =>
                      data.strFromAccountType
                        .toLowerCase()
                        .includes(searchQuery.toLowerCase()) ||
                      data.strFromAccountNo
                        .toLowerCase()
                        .includes(searchQuery.toLowerCase()) ||
                      data.strFromAccountName
                        .toLowerCase()
                        .includes(searchQuery.toLowerCase()) ||
                      data.strToAccountType
                        .toLowerCase()
                        .includes(searchQuery.toLowerCase())
                  )
                  .map((data) => (
                    <tr
                      key={uuidv4()}
                      className=" border-b dark:border-neutral-500 "
                    >
                      <td className="px-6 py-2.5 whitespace-nowrap flex text-center">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strFromAccountType || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strFromAccountNo || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strFromAccountName || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strToAccountType || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strToAccountNo || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strToAccountName || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 text-end whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strAmount || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strMakerId || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap ">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strNarration || "-"}
                        </div>
                      </td>
                    </tr>
                  ))}
              </tbody>
            </table>
          </div>
        </div>

        <div className="bg-gray-100 px-4 py-1 text-right sm:px-6 ">
          <button
            title="Verify Data"
            type="button"
            onClick={verify}
            data-modal-toggle="defaultModal"
            className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3  text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
          >
            Verify
          </button>
        </div>
      </div>
      {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
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
